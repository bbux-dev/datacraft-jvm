package org.datacraft

import org.datacraft.casters.MultiCaster
import org.datacraft.loaders.CharClassAbbreviationLoader
import org.datacraft.loaders.CharClassMappings
import org.datacraft.models.ValueSupplierLoader
import org.datacraft.suppliers.CastSupplier
import org.datacraft.suppliers.DecoratedSupplier
import java.util.*

internal object Loaders {
    fun init(spec: DataSpec, enforceSchema: Boolean = false, dataDir: String? = null): Loader {
        val serviceLoader = ServiceLoader.load(ValueSupplierLoader::class.java)
        val mapping = mutableMapOf<String, ValueSupplierLoader<*>>()
        for (typeLoader in serviceLoader) {
            for (name in typeLoader.typeNames()) {
                mapping[name] = typeLoader
            }
        }
        // special case for char_class
        for (key in CharClassMappings.CLASS_MAPPING.keys) {
            mapping["cc-$key"] = CharClassAbbreviationLoader(key)
        }
        return Loader(spec, mapping, enforceSchema, dataDir)
    }

    fun configuredTypes(): List<String> {
        val serviceLoader = ServiceLoader.load(ValueSupplierLoader::class.java)
        val types = serviceLoader.flatMap { e -> e.typeNames() }
        val charClassAbbreviations = CharClassMappings.CLASS_MAPPING.keys.map { "cc-$it" }
        return types + charClassAbbreviations
    }
}

class Loader(
    private val spec: DataSpec,
    private val mapping: MutableMap<String, ValueSupplierLoader<*>>,
    private val enforceSchema: Boolean = false,
    val dataDir: String?
) {

    private val cache = mutableMapOf<String, ValueSupplier<Any>>()

    @Suppress("UNCHECKED_CAST")
    fun get(field: String): ValueSupplier<Any> {
        // check cache first
        cache[field]?.let { return it }

        val fieldSpec = spec.data[field] ?: spec.refs[field] ?: throw SpecException("Unknown field name $field")
        val typeName = fieldSpec.type
        val loader = mapping[typeName] ?: throw SpecException("No type with name $typeName discovered")

        var supplier = loader.load(fieldSpec, this)
        val config: Map<String, Any> = fieldSpec.config ?: mapOf()
        if (config.containsKey("cast")) {
            val names = (config["cast"] as String).split(";")
            val casters = names.map { Registries.casterFor(it) }
            supplier = if (casters.size > 1) {
                CastSupplier(supplier as ValueSupplier<Any>, MultiCaster(casters))
            } else {
                CastSupplier(supplier as ValueSupplier<Any>, casters[0])
            }
        }
        if (isDecorated(config)) {
            supplier = DecoratedSupplier(supplier = supplier, config = config)
        }
        cache[field] = supplier as ValueSupplier<Any>
        return supplier
    }

    private fun isDecorated(config: Map<String, Any>) = config.containsKey("prefix") || config.containsKey("suffix")
}