package org.datacraft

interface ValueSupplierLoader<T> {
    fun typeName() : String
    fun load(spec: FieldSpec, loader: Loader): ValueSupplier<T>
}