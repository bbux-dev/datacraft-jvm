package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class RangeLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("range")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        if (spec.data == null) {
            throw SpecException("data cannot be null for range spec")
        }
        if (spec.data !is List<*> || spec.data.isEmpty()) {
            throw SpecException("data element for ranges type must be list with at least one element: $spec")
        }

        val start : Int
        val end : Int


        if (spec.data.size == 1) {
            start = 0
            end = dataToInt(spec.data, 0, spec)
        } else {
            start =  dataToInt(spec.data, 0, spec)
            end =  dataToInt(spec.data, 1, spec)
        }
        val step : Int = if (spec.data.size > 2) {
            dataToInt(spec.data, 2, spec)
        } else {
            1
        }
        return Suppliers.range(start, end, step)

    }

    private fun dataToInt(data: List<*>, index: Int, spec: FieldSpec) =
        (data[index] as? Number)?.toInt() ?: throw SpecException("Invalid end value for $spec")
}