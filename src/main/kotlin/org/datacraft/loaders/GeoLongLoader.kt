package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class GeoLongLoader : ValueSupplierLoader<Double> {
    override fun typeNames(): List<String> = listOf("geo.long")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Double> {
        val config = spec.config ?: mapOf()
        return getLongitudeSupplier(config)
    }
}