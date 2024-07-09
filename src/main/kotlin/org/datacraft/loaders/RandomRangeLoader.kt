package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class RandomRangeLoader : ValueSupplierLoader<Double> {
    override fun typeNames(): List<String> = listOf("rand_range")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Double> {
        return randomRangeSupplier(spec, spec.data)
    }
}
internal fun randomRangeSupplier(
    spec: FieldSpec,
    data: Any?
): ValueSupplier<Double> {
    if (data == null) {
        throw SpecException("data cannot be null for rand_range spec")
    }
    if (data !is List<*> || data.isEmpty()) {
        throw SpecException("data element for ranges type must be list with at least one element: $spec")
    }
    val start = if (data.size == 1) {
        0.0
    } else {
        data[0].toString().toDouble()
    }
    val end = if (data.size == 1) {
        data[0].toString().toDouble()
    } else {
        data[1].toString().toDouble()
    }

    return Suppliers.randomRange(
        start = start,
        end = end
    )
}