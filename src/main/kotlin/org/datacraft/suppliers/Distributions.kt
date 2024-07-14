package org.datacraft.suppliers

import org.datacraft.models.Distribution
import java.util.*

object Distributions {
    fun fromString(countDist: String): Distribution {
        // TODO: Implement this proper
        return Uniform(0.0, 100.0)
    }

    /**
     * Implements a uniform distribution between a specified start and end range.
     *
     * @property startVal The starting value of the range.
     * @property endVal The ending value of the range.
     */
    class Uniform(private val startVal: Double, private val endVal: Double) : Distribution {
        private val random = Random()

        /**
         * Generates a random Double value uniformly distributed between [startVal] and [endVal].
         * @return A Double value uniformly distributed within the specified range.
         */
        override fun nextValue(): Double {
            return random.nextDouble(startVal, endVal)
        }
    }

    /**
     * Implements a normal (Gaussian) distribution with a specified mean and standard deviation.
     *
     * @property mean The mean (average) value of the distribution.
     * @property stddev The standard deviation, which measures the amount of variation or dispersion from the mean.
     */
    class Normal(private val mean: Double, private val stddev: Double) : Distribution {
        private val random = Random()

        /**
         * Generates a random Double value normally distributed with the specified [mean] and [stddev].
         * @return A Double value normally distributed based on the specified mean and standard deviation.
         */
        override fun nextValue(): Double {
            return (mean + stddev * random.nextGaussian()).toDouble()
        }
    }
}