package org.datacraft.models

/**
 * Represents a distribution of values. Implementations of this interface provide mechanisms to generate random
 * values based on specific distributions.
 */
interface Distribution {
    /**
     * Generates the next value according to this distribution.
     * @return A Double value generated based on the distribution's characteristics.
     */
    fun nextValue(): Double
}