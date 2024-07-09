package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class GeoPairLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("geo.pair")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        val config = spec.config ?: mapOf()
        val latSupplier = getLatitudeSupplier(config)
        val longSupplier = getLongitudeSupplier(config)
        val latFirst = getConfigValue(config, "lat_first", false)

        val suppliers = if (latFirst) {
            listOf(latSupplier, longSupplier)
        } else {
            listOf(longSupplier, latSupplier)
        }

        val joinWith = getConfigValue(config, "join_with", ",")
        val asList = getConfigValue(config, "as_list", false)

        return Suppliers.combine(suppliers, joinWith, asList)
    }


}

internal fun getLongitudeSupplier(config: Map<String, Any>): ValueSupplier<Double> {
    val startLong: Double = getConfigValue(config, "start_long", -180.0)
    val endLong: Double = getConfigValue(config, "end_long", 180.0)
    if (startLong < -180 || startLong > 180) {
        throw SpecException("start_long out of range")
    }
    if (endLong < -180 || endLong > 180) {
        throw SpecException("end_long out of range")
    }
    val longSupplier = Suppliers.randomRange(startLong, endLong)
    return longSupplier
}

internal fun getLatitudeSupplier(config: Map<String, Any>): ValueSupplier<Double> {
    val startLat: Double = getConfigValue(config, "start_lat", -90.0)
    val endLat: Double = getConfigValue(config, "end_lat", 90.0)
    if (startLat < -90 || startLat > 90) {
        throw SpecException("start_lat out of range")
    }
    if (endLat < -90 || endLat > 90) {
        throw SpecException("end_lat out of range")
    }
    val latSupplier = Suppliers.randomRange(startLat, endLat)
    return latSupplier
}