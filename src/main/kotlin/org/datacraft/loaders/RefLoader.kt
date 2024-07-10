package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader


class RefLoader : ValueSupplierLoader<Any> {

    override fun typeNames(): List<String> = listOf("ref")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        val refSpec = spec as? RefFieldSpec
            ?: throw IllegalArgumentException("Spec must be a RefFieldSpec")
        return loader.get(refSpec.ref)
    }
}