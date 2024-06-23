package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class RangeLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("range")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        if (spec.data == null) {
            throw SpecException("data cannot be null for range spec")
        }
        if (spec.data !is List<*>) {
            throw SpecException(" data element for ranges type must be list with at least one element: $spec")
        }
        return Suppliers.range(
            start = spec.data[0] as Int,
            end = spec.data[1] as Int
        )
    }
}