package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class CharClassAbbreviationLoader(private val data : String) : CharClassLoader() {
    override fun typeNames(): List<String> = listOf("cc-")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<String> {
        val updatedSpec = FieldSpec.basic("char_class", data, spec.config ?: mapOf())
        return super.load(updatedSpec, loader)
    }
}