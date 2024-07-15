package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader
import org.datacraft.suppliers.ReplaceSupplier

class ReplaceLoader : ValueSupplierLoader<String> {

    override fun typeNames(): List<String> {
        return listOf("replace")
    }

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<String> {
        val (mappings, wrapped) = validateAndLoadReplace(spec, loader)
        return ReplaceSupplier(wrapped = wrapped, replacements = mappings)
    }

    private fun validateAndLoadReplace(fieldSpec: FieldSpec, loader: Loader): Pair<Map<String, ValueSupplier<Any>>, ValueSupplier<Any>> {
        val spec = fieldSpec as ReplaceFieldSpec

        val wrapped = loader.get(spec.ref)
        val mappings = spec.data
        if (mappings !is Map<*, *>) {
            throw SpecException("data element must be dictionary for replace spec: $fieldSpec")
        }

        // Turn values into value suppliers
        val valueSuppliers: Map<String, ValueSupplier<Any>> = mappings.map { (key, value) ->
            key.toString() to Suppliers.values(value!!)
        }.toMap()

        return Pair(valueSuppliers, wrapped)
    }
}