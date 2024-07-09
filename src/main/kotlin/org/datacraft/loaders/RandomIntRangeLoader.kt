package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.casters.IntCaster
import org.datacraft.models.ValueSupplierLoader

class RandomIntRangeLoader : ValueSupplierLoader<Int> {
    override fun typeNames(): List<String> = listOf("rand_int_range")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Int> {
        val supplier : ValueSupplier<Double> = randomRangeSupplier(spec, spec.data)
        @Suppress("UNCHECKED_CAST")
        return Suppliers.cast(supplier as ValueSupplier<Any>, IntCaster())
    }

}

