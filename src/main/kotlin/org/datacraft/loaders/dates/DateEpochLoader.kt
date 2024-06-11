package org.datacraft.loaders.dates

import org.datacraft.FieldSpec
import org.datacraft.Loader
import org.datacraft.ValueSupplier
import org.datacraft.models.ValueSupplierLoader
import org.datacraft.suppliers.Dates


class DateEpochLoader : ValueSupplierLoader<Long> {
    override fun typeNames(): List<String> = listOf("date.epoch")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Long> {
        val updatedConfig: MutableMap<String, Any> = spec.config?.toMutableMap() ?: mutableMapOf()
        return Dates.epochDate(asMillis = false, updatedConfig)
    }
}