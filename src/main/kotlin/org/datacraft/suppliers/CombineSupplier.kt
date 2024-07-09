package org.datacraft.suppliers

import org.datacraft.ValueSupplier

class CombineSupplier(private val suppliers: List<ValueSupplier<*>>,
                      private val joinWith: String?,
                      private val asList: Boolean?) : ValueSupplier<Any> {
    override fun next(iteration: Long): Any {
        val results = suppliers.map { supplier ->
            supplier.next(iteration).toString()
        }
        if (asList == true) {
            return results
        }
        // Join all strings into a single string
        return results.joinToString(joinWith ?: "")
    }
}