package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class GeoLongLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("geo.long")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        val config = spec.config ?: mapOf()
        val startLat: Double = getConfigValue(config, "start_long", -180.0)
        val endLat: Double = getConfigValue(config, "end_long", 180.0)
        if (startLat < -180 || startLat > 180) {
            throw SpecException("start_long out of range")
        }
        if (endLat < -180 || endLat > 180) {
            throw SpecException("end_long out of range")
        }
        return Suppliers.randomRange(startLat, endLat)
    }
}