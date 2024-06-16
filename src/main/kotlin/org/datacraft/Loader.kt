package org.datacraft

import org.datacraft.models.ValueSupplierLoader
import java.util.*

object Loaders {
    fun init(spec: DataSpec, enforceSchema: Boolean = false, dataDir: String? = null) : Loader {
        val serviceLoader = ServiceLoader.load(ValueSupplierLoader::class.java)
        val mapping = mutableMapOf<String, ValueSupplierLoader<*>>()
        for (typeLoader in serviceLoader) {
            for (name in typeLoader.typeNames()) {
                mapping[name] = typeLoader
            }
        }
        return Loader(spec, mapping, enforceSchema, dataDir)
    }

    fun configuredTypes() : List<String> {
        val serviceLoader = ServiceLoader.load(ValueSupplierLoader::class.java)
        return serviceLoader.flatMap { e -> e.typeNames() }
    }
}

class Loader(private val spec: DataSpec,
             private val mapping: MutableMap<String, ValueSupplierLoader<*>>,
             private val enforceSchema: Boolean = false,
             val dataDir: String?) {

    private val cache = mutableMapOf<String, ValueSupplier<Any>>()

    fun get(field: String) : ValueSupplier<Any> {
        // check cache first
        cache[field]?.let { return it }

        val fieldSpec = spec.data[field] ?: spec.refs[field] ?: throw SpecException("Unknown field name $field")
        val typeName = fieldSpec.type
        val loader = mapping[typeName] ?: throw SpecException("No type with name $typeName discovered")

        val supplier = loader.load(fieldSpec, this)
        @Suppress("UNCHECKED_CAST")
        cache[field] = supplier as ValueSupplier<Any>
        return supplier
    }
}