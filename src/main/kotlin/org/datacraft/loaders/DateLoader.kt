package org.datacraft.loaders

import org.datacraft.FieldSpec
import org.datacraft.ValueSupplier
import org.datacraft.ValueSupplierLoader
import org.datacraft.suppliers.Dates

class DateLoader : ValueSupplierLoader<String> {
    override fun load(spec: FieldSpec, loader: Any?): ValueSupplier<String> {
        return Dates.date(spec.config)
    }
}
