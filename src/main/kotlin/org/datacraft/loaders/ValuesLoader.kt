package org.datacraft.loaders

import org.datacraft.*

class ValuesLoader : ValueSupplierLoader<Any> {
    override fun typeName(): String = "values"

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        if (spec.data == null) {
            throw SpecException("data cannot be null for values spec")
        }
        return Suppliers.values(spec.data)
    }
}

