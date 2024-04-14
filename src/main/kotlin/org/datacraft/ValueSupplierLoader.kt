package org.datacraft

interface ValueSupplierLoader<T> {
    fun load(spec: FieldSpec, loader: Any?): ValueSupplier<T>
}