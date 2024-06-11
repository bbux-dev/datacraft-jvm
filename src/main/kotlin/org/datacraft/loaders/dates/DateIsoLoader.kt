package org.datacraft.loaders.dates

import org.datacraft.FieldSpec
import org.datacraft.Loader
import org.datacraft.ValueSupplier
import org.datacraft.models.ValueSupplierLoader
import org.datacraft.suppliers.Dates

const val ISO_FORMAT_NO_MICRO = "yyyy-MM-dd'T'HH:mm:ss"

class DateIsoLoader : ValueSupplierLoader<String> {
    override fun typeNames(): List<String> = listOf("date.iso")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<String> {
        val updatedConfig: MutableMap<String, Any> = spec.config?.toMutableMap() ?: mutableMapOf()
        updatedConfig["format"] = ISO_FORMAT_NO_MICRO
        return Dates.date(updatedConfig)
    }
}