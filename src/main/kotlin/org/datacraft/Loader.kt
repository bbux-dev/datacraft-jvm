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
}

class Loader(private val spec: DataSpec, private val mapping: MutableMap<String, ValueSupplierLoader<*>>) {
    fun get(field: String) : ValueSupplier<out Any?> {
        val fieldSpec = spec.data[field] ?: throw SpecException("Unknown field name $field")
        val typeName = fieldSpec.type
        val loader = mapping[typeName] ?: throw SpecException("No type with name $typeName discovered")

        return loader.load(fieldSpec, this)
    }
}