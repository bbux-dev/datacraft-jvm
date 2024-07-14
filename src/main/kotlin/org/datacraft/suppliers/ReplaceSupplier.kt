package org.datacraft.suppliers

import org.datacraft.ValueSupplier

class ReplaceSupplier(
    private val wrapped: ValueSupplier<*>,
    private val replacements: Map<String, String>
) : ValueSupplier<String> {
    override fun next(iteration: Long): String {
        var value = wrapped.next(iteration).toString()
        for ((oldValue, newValue) in replacements) {
            value = value.replace(oldValue, newValue)
        }
        return value
    }
}