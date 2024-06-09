package org.datacraft.loaders.dates

import org.datacraft.FieldSpec
import org.datacraft.Loader
import org.datacraft.ValueSupplier
import org.datacraft.ValueSupplierLoader
import org.datacraft.suppliers.Dates

const val ISO_FORMAT_WITH_MILLIS = "YYYY-mm-dd'T'HH:mm:ss.SSS"

class DateIsoMillisLoader : ValueSupplierLoader<String> {
    override fun typeName(): String = "date.iso.ms"

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<String> {
        val updatedConfig: MutableMap<String, Any> = spec.config?.toMutableMap() ?: mutableMapOf()
        updatedConfig["format"] = ISO_FORMAT_WITH_MILLIS
        return Dates.date(updatedConfig)
    }
}