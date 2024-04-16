package org.datacraft

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
    fun values(spec: FieldSpec): ValueSupplier<Any?> {
        return when (spec.data) {
            is List<*> -> {
                @Suppress("UNCHECKED_CAST")
                (Common.ListValueSupplier(spec.data as List<Any>))
            }

            is Map<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                (Common.WeightedValueSupplier(spec.data as Map<Any, Any>))
            }

            else -> {
                Common.ConstantValueSupplier(spec.data)
            }
        }
    }

}