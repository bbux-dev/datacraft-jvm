package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class CombineListLoader : ValueSupplierLoader<Any> {
    override fun typeNames(): List<String> = listOf("combine-list")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Any> {
        val combineSpec = spec as CombineListFieldSpec

        val suppliersList = combineSpec.refs.map { refs ->
            val updated = CombineFieldSpec(spec.config, refs)
            loadCombine(updated, refs, loader)
        }

        return Suppliers.fromListOfSuppliers(suppliersList, true)
    }


    private fun loadCombine(spec: FieldSpec, keys: List<String>, loader: Loader): ValueSupplier<Any> {
        val toCombine = keys.map { key ->
            loader.get(key)
        }

        val config = spec.config ?: emptyMap()
        val asList = config["as_list"] as? Boolean ?: Registries.getDefault("combine_as_list") as Boolean
        val joiner = config["join_with"] as? String ?: Registries.getDefault("combine_join_with") as String

        return Suppliers.combine(toCombine, joiner, asList)
    }
}
