package org.datacraft

import org.datacraft.suppliers.CombineSupplier
import org.datacraft.suppliers.Common


object Suppliers {
    /**
     * Creates a [ValueSupplier] based on the provided [FieldSpec]. This function supports creating different types of [ValueSupplier]
     * depending on the type of data contained within the [FieldSpec]:
     * - [ListValueSupplier] for lists of values.
     * - [WeightedValueSupplier] for maps.
     * - [ConstantValueSupplier] for constants (primitives, strings, or object versions of primitives).
     *
     * @param spec The specification containing the data for the [ValueSupplier].
     * @return A [ValueSupplier] instance capable of supplying values as per the [spec].
     * @throws IllegalArgumentException if the spec contains data types that are not supported.
     */
    fun values(data: Any): ValueSupplier<Any> {
        return when (data) {
            is List<*> -> {
                @Suppress("UNCHECKED_CAST")
                (Common.ListValueSupplier(data as List<Any>))
            }

            is Map<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                (Common.WeightedValueSupplier(data as Map<Any, Any>))
            }

            else -> {
                Common.ConstantValueSupplier(data)
            }
        }
    }

    fun combine(suppliers: List<ValueSupplier<Any>>): ValueSupplier<Any?> {
        return CombineSupplier(suppliers)
    }

}