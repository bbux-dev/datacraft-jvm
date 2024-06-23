package org.datacraft.suppliers

import org.datacraft.ValueSupplier
import kotlin.random.Random

/**
 * A [ValueSupplier] that supplies values from a predefined list. Each call to [next] returns the next value in the list,
 * cycling back to the beginning of the list when the end is reached.
 *
 * @param T The type of values in the list.
 * @property values The list of values to supply.
 */
class ListValueSupplier<T>(private val values: List<T>) : ValueSupplier<T> {
    /**
     * Returns the value at the current iteration's index within the list, cycling through the list if necessary.
     * @param iteration The current iteration count.
     * @return The value at the calculated index of the list.
     */
    override fun next(iteration: Long): T = values[((iteration - 1) % values.size).toInt()]
}

/**
 * Supplies a constant value for every call.
 *
 * @param T The type of the value to supply.
 * @property value The constant value to be supplied.
 */
class ConstantValueSupplier<T>(private val value: T) : ValueSupplier<T> {
    override fun next(iteration: Long): T = value
}

/**
 * Supplies values based on their weighted probabilities. The weights are specified in a map,
 * where each entry consists of a value and its corresponding weight. The weights do not need
 * to sum to 1; they will be normalized internally.
 *
 * @property weights A map where keys are the values to supply and values are the weights of these values.
 */
class WeightedValueSupplier(private val weights: Map<Any, Any>) : ValueSupplier<Any> {
    private val cumulativeWeights: List<Pair<Any, Double>>
    private val defaultValue: Any

    init {
        val validatedWeights = weights.mapValues { (_, value) ->
            when (value) {
                is Double -> value
                is Float -> value.toDouble()
                is Int -> value.toDouble()
                else -> throw IllegalArgumentException("All values in the weights map must be of type Double, Float, or Integer")
            }
        }
        // Normalize weights and calculate cumulative weights for efficient selection
        val totalWeight = validatedWeights.values.sum()
        var sum = 0.0
        var maxWeight = Double.MIN_VALUE
        var tempDefaultValue: Any? = null

        cumulativeWeights = validatedWeights.map { (value, weight) ->
            // Update maxWeight and defaultValue if current weight is greater
            if (weight > maxWeight) {
                maxWeight = weight
                tempDefaultValue = value
            }
            sum += weight / totalWeight
            value to sum
        }

        defaultValue = tempDefaultValue ?: throw IllegalArgumentException("Weights map cannot be empty")

        // Ensure the list is sorted by cumulative weight, just in case
        cumulativeWeights.sortedBy { it.second }
    }

    /**
     * Selects a value based on its weighted probability.
     *
     * @param iteration Ignored in this implementation, as the value selection is random.
     * @return A value selected based on its weighted probability.
     */
    override fun next(iteration: Long): Any {
        val randomValue = Random.nextDouble()
        // Find the first entry in the cumulative weights that is greater than or equal to the random value
        val foundPair = cumulativeWeights.find { (_, cumWeight) -> randomValue < cumWeight }
        return foundPair?.first ?: defaultValue
    }
}