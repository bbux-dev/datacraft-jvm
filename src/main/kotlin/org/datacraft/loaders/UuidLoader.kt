package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader
import java.util.UUID

class UuidLoader : ValueSupplierLoader<String> {
    override fun typeNames(): List<String> = listOf("uuid")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<String> {
        return Suppliers.functionSupplier { _ -> UUID.randomUUID().toString() }
    }
}