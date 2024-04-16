package org.datacraft.loaders

import org.datacraft.*

class ValuesLoader : ValueSupplierLoader<Any?> {
    override fun typeName(): String = "values"

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any?> {
        return Suppliers.values(spec)
    }
}

