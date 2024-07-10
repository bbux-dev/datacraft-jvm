package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader


class RefLoader : ValueSupplierLoader<Any> {

    override fun typeNames(): List<String> = listOf("ref")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        val key = when {
            spec.ref != null -> spec.ref
            spec.data != null -> spec.data
            else -> throw SpecException("No key found for spec: $spec")
        }
        return loader.get(key.toString())
    }
}