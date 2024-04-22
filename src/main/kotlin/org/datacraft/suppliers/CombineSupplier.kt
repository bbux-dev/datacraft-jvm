package org.datacraft.suppliers

import org.datacraft.ValueSupplier

class CombineSupplier(private val suppliers : List<ValueSupplier<Any>>) : ValueSupplier<Any?> {
    override fun next(iteration: Long): String {
        val results = suppliers.map { supplier ->
            supplier.next(iteration).toString()  // Call next(i) and convert to string
        }
        // Join all strings into a single string
        return results.joinToString("")
    }
}