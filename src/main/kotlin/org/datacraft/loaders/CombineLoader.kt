package org.datacraft.loaders

import org.datacraft.*

class CombineLoader : ValueSupplierLoader<Any?> {
    override fun typeName(): String = "combine"

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any?> {
        val suppliers: MutableList<ValueSupplier<Any>> = mutableListOf()
        val combineSpec = spec as? CombineFieldSpec
            ?: throw IllegalArgumentException("Spec must be a CombineFieldSpec")
        for (key in combineSpec.refs) {
            suppliers.add(loader.get(key))
        }
        for (key in combineSpec.fields) {
            suppliers.add(loader.get(key))
        }
        return Suppliers.combine(suppliers.toList())
    }
}
