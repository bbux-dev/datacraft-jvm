package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class GeoLatLoader : ValueSupplierLoader<Double> {
    override fun typeNames(): List<String> = listOf("geo.lat")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Double> {
        val config = spec.config ?: mapOf()
        return getLatitudeSupplier(config)
    }
}