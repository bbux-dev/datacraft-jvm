package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class GeoLongLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("geo.long")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        val config = spec.config ?: mapOf()
        return getLongitudeSupplier(config)
    }
}