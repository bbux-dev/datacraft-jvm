package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader
import org.datacraft.suppliers.WeightedRefSupplier

class WeightedRefLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("weighted_ref")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        @Suppress("UNCHECKED_CAST")
        val data = spec.data as? Map<String, Any>
            ?: throw SpecException("Data is missing or not a map in spec: $spec")

        val keySupplier = Suppliers.values(data)
        val valuesMap = data.mapValues { (key, _) -> loader.get(key) }

        val supplier = WeightedRefSupplier(keySupplier, valuesMap)

        // TODO: handle count in config

        return supplier
    }
}
