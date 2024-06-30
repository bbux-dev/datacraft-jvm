package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class SampleLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("sample")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        if (spec.data == null) {
            throw SpecException("data cannot be null for sample spec")
        }

        val dataList: List<Any> = when (spec.data) {
            is List<*> -> spec.data as List<Any>
            else -> throw SpecException("data must be a list for sample spec")
        }

        return Suppliers.sample(dataList)
    }
}