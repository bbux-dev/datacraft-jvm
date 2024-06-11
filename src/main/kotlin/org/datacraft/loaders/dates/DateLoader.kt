package org.datacraft.loaders.dates

import org.datacraft.FieldSpec
import org.datacraft.Loader
import org.datacraft.ValueSupplier
import org.datacraft.models.ValueSupplierLoader
import org.datacraft.suppliers.Dates


class DateLoader : ValueSupplierLoader<String> {
    override fun typeNames(): List<String> = listOf("date")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<String> {
        val updatedConfig: MutableMap<String, Any> = spec.config?.toMutableMap() ?: mutableMapOf()
        // TODO: update config with specifics and defaults
        return Dates.date(updatedConfig)
    }
}

