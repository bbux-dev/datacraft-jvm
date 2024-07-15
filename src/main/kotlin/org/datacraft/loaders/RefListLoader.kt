package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class RefListLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("ref_list")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        val refListSpec = spec as RefListFieldSpec
        val supplierList : List<ValueSupplier<Any>> = refListSpec.refs.map { loader.get(it) }
        return Suppliers.combine(supplierList, asList = true)
    }
}