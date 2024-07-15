package org.datacraft

import org.datacraft.casters.IntCaster
import org.datacraft.models.Caster
import org.datacraft.models.Distribution
import org.datacraft.suppliers.*

/**
 * A utility class that provides various suppliers for generating values.
 *
 * The `Suppliers` class contains static methods for creating instances of `ValueSupplier` with different behaviors,
 * such as generating values within a range, sampling from a list, or applying string replacements.
 * Each supplier can be used to generate values based on specific criteria or transformations.
 *
 * Some available suppliers include:
 * - `range`: Generates integer values within a specified range with a given step.
 * - `randomRange`: Generates random double values within a specified range.
 * - `functionSupplier`: Generates values by applying a provided function to the current iteration count.
 * - `sample`: Generates values by randomly sampling from a provided list.
 * - `ipV4Address`: Generates IPv4 addresses based on a specified CIDR block.
 * - `cast`: Converts values from one type to another using a provided caster.
 * - `characterClass`: Generates strings by sampling from character data with optional statistical parameters.
 * - `countSupplier`: Generates integer values based on a provided count or count distribution.
 * - `replace`: Generates values by applying replacements to the output of another supplier.
 */
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
     * Creates a `ValueSupplier` that generates a constant value.
     *
     * This function produces a `ValueSupplier` that always returns the same value.
     *
     * @param T The type of the constant value.
     * @param data The constant value to be supplied.
     * @return Returns a `ValueSupplier<T>` that supplies the constant value.
     */
    private fun <T> constant(data: T): ValueSupplier<T> {
        return ConstantValueSupplier(data)
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
    fun combine(
        suppliers: List<ValueSupplier<*>>,
        joinWith: String? = null,
        asList: Boolean? = false
    ): ValueSupplier<Any> {
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
    fun range(start: Int, end: Int, step: Int? = 1): ValueSupplier<Int> {
        return IntRangeSupplier(start, end, step)
    }
    fun range(range: IntRange, step: Int? = 1): ValueSupplier<Int> {
        return IntRangeSupplier(range.start, range.last, step)
    }
    /**
     * Creates a `ValueSupplier` that generates random values within a specified range.
     *
     * This function produces a `ValueSupplier` that generates double values between the specified `start` and `end` values.
     * The `start` value is inclusive, while the `end` value is exclusive.
     *
     * @param start The starting value of the range (inclusive).
     * @param end The ending value of the range (exclusive).
     * @return Returns a `ValueSupplier<Double>` that supplies double values within the specified range.
     */
    fun randomRange(start: Double, end: Double): ValueSupplier<Double> {
        return RandomRangeSupplier(start, end)
    }

    /**
     * Creates a `FunctionSupplier` that generates values based on the provided function.
     *
     * This function produces a `ValueSupplier` that generates values of type `T` by applying the provided function to the current iteration count.
     *
     * @param T The type of values generated by the supplier.
     * @param function A function that takes a Long value representing the current iteration and returns a value of type `T`.
     * @return Returns a `ValueSupplier<T>` that supplies values based on the provided function.
     */
    internal class FunctionSupplier<T>(private val function: (Long) -> T) : ValueSupplier<T> {
        override fun next(iteration: Long): T {
            return function(iteration)
        }
    }

    /**
     * Creates a `FunctionSupplier` that generates values based on the provided function.
     *
     * This function produces a `ValueSupplier` that generates values of type `T` by applying the provided function to the current iteration count.
     *
     * @param T The type of values generated by the supplier.
     * @param function A function that takes a Long value representing the current iteration and returns a value of type `T`.
     * @return Returns a `ValueSupplier<T>` that supplies values based on the provided function.
     */
    fun <T> functionSupplier(function: (Long) -> T): ValueSupplier<T> {
        return FunctionSupplier(function)
    }

    /**
     * Creates a `ValueSupplier` that randomly samples from the provided list of data.
     *
     * This function produces a `ValueSupplier` that generates values by randomly sampling from the specified list.
     *
     * @param data A list of values to sample from.
     * @return Returns a `ValueSupplier<Any>` that supplies values by sampling from the provided list.
     */
    fun sample(data: List<Any>): ValueSupplier<Any> {
        return SampleSupplier(data)
    }

    /**
     * Creates a `ValueSupplier` that generates IPv4 addresses based on the specified CIDR block.
     *
     * This function produces a `ValueSupplier` that generates IPv4 addresses within the given CIDR block.
     *
     * @param cidr The CIDR block to generate IPv4 addresses from. If null, a default range is used.
     * @return Returns a `ValueSupplier<String>` that supplies IPv4 addresses.
     */
    fun ipV4Address(cidr: String?): ValueSupplier<String> {
        return IpSupplier(cidr)
    }

    /**
     * Creates a `CastSupplier` that converts values from one type to another using the provided caster.
     *
     * This function produces a `ValueSupplier` that generates values by casting the output of another supplier.
     *
     * @param T The type to cast the values to.
     * @param supplier The original `ValueSupplier` providing values to be cast.
     * @param caster The `Caster` that defines the conversion logic.
     * @return Returns a `ValueSupplier<T>` that supplies values of the specified type.
     */
    fun <T> cast(supplier: ValueSupplier<Any>, caster: Caster<T>): ValueSupplier<T> {
        return CastSupplier(supplier, caster)
    }

    /**
     * Creates a `ValueSupplier` that generates character classes based on the provided data and optional statistical parameters.
     *
     * This function produces a `ValueSupplier` that generates strings by sampling from the given character data. Optionally, the
     * generation can be based on statistical parameters such as mean, standard deviation, min, max, and count.
     *
     * @param data The string data to sample from.
     * @param mean Optional mean value for statistical sampling.
     * @param stddev Optional standard deviation value for statistical sampling.
     * @param min Optional minimum value for statistical sampling.
     * @param max Optional maximum value for statistical sampling.
     * @param count Optional count value for fixed sampling.
     * @param countDist Optional count distribution for statistical sampling.
     * @return Returns a `ValueSupplier<String>` that supplies character classes based on the provided data and parameters.
     */
    fun characterClass(
        data: String,
        mean: Double? = null,
        stddev: Double? = null,
        min: Int? = null,
        max: Int? = null,
        count: Any? = null,
        countDist: String? = null
    ): ValueSupplier<String> {
        // Check for 'mean' and 'stddev' to return a stats sampler
        if (mean != null || stddev != null) {
            val counts = StatsCountSupplier(data.length, mean, stddev, min, max)
            return StringSamplerSupplier(data, counts)
        }
        return StringSamplerSupplier(data, countSupplier(count, countDist))
    }

    /**
     * Creates a `ValueSupplier` that generates integer values based on the provided count or count distribution.
     *
     * This function produces a `ValueSupplier` that generates integer values according to the specified count or distribution.
     *
     * @param count Optional fixed count value.
     * @param countDist Optional count distribution as a string.
     * @return Returns a `ValueSupplier<Int>` that supplies integer values based on the provided count or distribution.
     */
    fun countSupplier(
        count: Any? = null,
        countDist: String? = null
    ): ValueSupplier<Int> {
        var actualData: Any? = 1

        if (count != null) {
            actualData = count
        }
        if (countDist != null) {
            actualData = Distributions.fromString(countDist)
        }

        return when (actualData) {
            is List<*> -> cast(values(actualData as List<Any>), IntCaster())
            is Map<*, *> -> cast(values(actualData as Map<String, Double>), IntCaster())
            is Distribution -> cast(distribution(actualData), IntCaster())
            else -> try {
                constant((actualData as? Int ?: actualData.toString().toInt()))
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid count param: $actualData", e)
            }
        }
    }

    /**
     * Creates a `ValueSupplier` that generates values based on the provided distribution.
     *
     * This function produces a `ValueSupplier` that generates values according to the specified distribution.
     *
     * @param dist The distribution to generate values from.
     * @return Returns a `ValueSupplier<Any>` that supplies values based on the provided distribution.
     */
    private fun distribution(dist: Distribution): ValueSupplier<Any> {
        @Suppress("UNCHECKED_CAST")
        return DistributionSupplier(dist) as ValueSupplier<Any>
    }

    /**
     * Creates a `ReplaceSupplier` that generates values by applying replacements to the output of another supplier.
     *
     * This function produces a `ValueSupplier` that generates string values by replacing specified substrings in the output
     * of the wrapped supplier with the provided replacements.
     *
     * @param wrapped The original `ValueSupplier` providing values to be processed.
     * @param replacements A map of substrings to their replacement values.
     * @return Returns a `ValueSupplier<String>` that supplies string values with replacements applied.
     *
     * @sample org.datacraft.Suppliers.sampleReplaceUsage
     */
    fun replace(wrapped: ValueSupplier<*>, replacements: Map<String, Any>): ValueSupplier<String> {
        val valueSuppliers: Map<String, ValueSupplier<Any>> = replacements.map { (key, value) ->
            key to values(value)
        }.toMap()
        return ReplaceSupplier(wrapped, valueSuppliers)
    }

    /** Examples **/

    @JvmStatic
    internal fun sampleReplaceUsage() {
        val wrappedSupplier: ValueSupplier<Any> = Suppliers.values(listOf("@one$", "@two$", "@tre$"))
        val replacements = mapOf("@" to "AT:", "$" to "")
        val replaceSupplier = Suppliers.replace(wrappedSupplier, replacements)
        println(replaceSupplier.next(1))
    }
}