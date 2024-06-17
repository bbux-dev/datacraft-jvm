package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class RandomRangeLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("rand_range")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        if (spec.data == null) {
            throw SpecException("data cannot be null for rand_range spec")
        }
        if (spec.data !is List<*>) {
            throw SpecException(" data element for ranges type must be list with at least one element: $spec")
        }
        return Suppliers.randomRange(
            start = spec.data[0] as Double,
            end = spec.data[1] as Double
        )
    }
}