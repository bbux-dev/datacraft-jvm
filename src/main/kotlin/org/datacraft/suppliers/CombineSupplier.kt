package org.datacraft.suppliers

import org.datacraft.ValueSupplier

class CombineSupplier(private val suppliers: List<ValueSupplier<Any>>,
                      private val joinWith: String?,
                      private val asList: Boolean?) : ValueSupplier<Any> {
    override fun next(iteration: Long): Any {
        val results = suppliers.map { supplier ->
            supplier.next(iteration).toString()  // Call next(i) and convert to string
        }
        if (asList == true) {
            return results
        }
        // Join all strings into a single string
        return results.joinToString(joinWith ?: "")
    }
}