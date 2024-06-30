package org.datacraft

import org.datacraft.suppliers.*


object Suppliers {
    /**
     * Creates a [ValueSupplier] based on the provided [FieldSpec]. This function supports creating different types of [ValueSupplier]
     * depending on the type of data contained within the [FieldSpec]:
     * - [ListValueSupplier] for lists of values.
     * - [WeightedValueSupplier] for maps.
     * - [ConstantValueSupplier] for constants (primitives, strings, or object versions of primitives).
     *
     * @param data The data for the [ValueSupplier].
     * @return A [ValueSupplier] instance capable of supplying values for this type of data (list, constant, wighted)
     */
    fun values(data: Any): ValueSupplier<Any> {
        return when (data) {
            is List<*> -> {
                @Suppress("UNCHECKED_CAST")
                (ListValueSupplier(data as List<Any>))
            }

            is Map<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                (WeightedValueSupplier(data as Map<Any, Any>))
            }

            else -> {
                ConstantValueSupplier(data)
            }
        }
    }

    /**
     * Combines multiple values from different suppliers into one value.
     *
     * This function creates a `CombineSupplier` that aggregates the output of several `ValueSupplier` instances.
     * By default, it combines the values into a single string separated by the `joinWith` delimiter if provided.
     * If `asList` is set to true, it combines the values into a list instead of a string.
     *
     * @param suppliers A list of `ValueSupplier<Any>` instances to be combined.
     * @param joinWith The delimiter used to join the values into a single string. If null, no delimiter is used.
     * @param asList A Boolean flag determining the format of the combined result.
     *               If true, results are combined into a list; if false or null, results are combined into a string.
     * @return Returns a `ValueSupplier<Any>` that either supplies the combined string or list, depending on `asList`.
     */
    fun combine(suppliers: List<ValueSupplier<Any>>,
                joinWith: String? = null,
                asList: Boolean? = false): ValueSupplier<Any> {
        return CombineSupplier(suppliers, joinWith, asList)
    }

    /**
     * Creates a `RangeSupplier` that generates values within a specified range.
     *
     * This function produces a `ValueSupplier` that generates integer values from the specified `start` to `end`.
     * The `start` value is inclusive, while the `end` value is also inclusive.
     *
     * @param start The starting value of the range (inclusive).
     * @param end The ending value of the range (exclusive).
     * @param step The increment size between iterations (default is 1)
     * @return Returns a `ValueSupplier<Any>` that supplies integer values within the specified range.
     */
    fun range(start: Int, end: Int, step: Int): ValueSupplier<Any> {
        return RangeSupplier(start, end, step)
    }

    fun randomRange(start: Double, end: Double): ValueSupplier<Any> {
        return RandomRangeSupplier(start, end)
    }

    class FunctionSupplier<T>(private val function: (Long) -> T) : ValueSupplier<T> {
        override fun next(iteration: Long): T {
            return function(iteration)
        }
    }

    fun <T> functionSupplier(function: (Long) -> T): ValueSupplier<T> {
        return FunctionSupplier(function)
    }

    fun sample(data: List<Any>): ValueSupplier<Any> {
        return SampleSupplier(data)
    }

}