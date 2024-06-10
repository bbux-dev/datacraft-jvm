package org.datacraft.loaders.dates

import org.datacraft.FieldSpec
import org.datacraft.Loader
import org.datacraft.ValueSupplier
import org.datacraft.ValueSupplierLoader
import org.datacraft.suppliers.Dates


class DateEpochMillisLoader : ValueSupplierLoader<Long> {
    override fun typeNames(): List<String> = listOf("date.epoch.ms", "date.epoch.millis")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Long> {
        val updatedConfig: MutableMap<String, Any> = spec.config?.toMutableMap() ?: mutableMapOf()
        return Dates.epochDate(asMillis = true, updatedConfig)
    }
}