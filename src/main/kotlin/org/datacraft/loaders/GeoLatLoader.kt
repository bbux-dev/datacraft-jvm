package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class GeoLatLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("geo.lat")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        val config = spec.config ?: mapOf()
        val startLat: Double = getConfigValue(config, "start_lat", -90.0)
        val endLat: Double = getConfigValue(config, "end_lat", 90.0)
        if (startLat < -90 || startLat > 90) {
            throw SpecException("start_lat out of range")
        }
        if (endLat < -90 || endLat > 90) {
            throw SpecException("end_lat out of range")
        }
        return Suppliers.randomRange(startLat, endLat)
    }
}