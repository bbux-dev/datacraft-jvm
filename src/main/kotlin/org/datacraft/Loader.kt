package org.datacraft

import java.util.*

object Loaders {
    fun init(spec: DataSpec) : Loader {
        val serviceLoader = ServiceLoader.load(ValueSupplierLoader::class.java)
        val mapping = mutableMapOf<String, ValueSupplierLoader<*>>()
        for (typeLoader in serviceLoader) {
            mapping[typeLoader.typeName()] = typeLoader
        }
        return Loader(spec, mapping)
    }

    fun configuredTypes() : List<String> {
        val serviceLoader = ServiceLoader.load(ValueSupplierLoader::class.java)
        return serviceLoader.map { e -> e.typeName() }
    }
}

class Loader(private val spec: DataSpec, private val mapping: MutableMap<String, ValueSupplierLoader<*>>) {

    private val cache = mutableMapOf<String, ValueSupplier<Any>>()

    fun get(field: String) : ValueSupplier<Any> {
        // check cache first
        cache[field]?.let { return it }

        val fieldSpec = spec.data[field] ?: throw SpecException("Unknown field name $field")
        val typeName = fieldSpec.type
        val loader = mapping[typeName] ?: throw SpecException("No type with name $typeName discovered")

        val supplier = loader.load(fieldSpec, this)
        @Suppress("UNCHECKED_CAST")
        cache[field] = supplier as ValueSupplier<Any>
        return supplier
    }
}