package org.datacraft.loaders.dates

import org.datacraft.FieldSpec
import org.datacraft.Loader
import org.datacraft.ValueSupplier
import org.datacraft.ValueSupplierLoader
import org.datacraft.suppliers.Dates

const val ISO_FORMAT_NO_MICRO = "YYYY-mm-yd'T'HH:MM:SS"

class DateIsoLoader : ValueSupplierLoader<String> {
    override fun typeName(): String = "date.iso"

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<String> {
        val updatedConfig: MutableMap<String, Any> = spec.config?.toMutableMap() ?: mutableMapOf()
        updatedConfig["format"] = ISO_FORMAT_NO_MICRO
        return Dates.date(updatedConfig)
    }
}