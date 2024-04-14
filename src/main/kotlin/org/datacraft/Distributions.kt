package org.datacraft

import org.datacraft.models.Distribution
import java.util.*

object Distributions {
    /**
     * Implements a uniform distribution between a specified start and end range.
     *
     * @property startVal The starting value of the range.
     * @property endVal The ending value of the range.
     */
    class Uniform(private val startVal: Long, private val endVal: Long) : Distribution {
        private val random = Random()

        /**
         * Generates a random long value uniformly distributed between [startVal] and [endVal].
         * @return A long value uniformly distributed within the specified range.
         */
        override fun nextValue(): Long {
            return random.nextLong(startVal, endVal)
        }
    }

    /**
     * Implements a normal (Gaussian) distribution with a specified mean and standard deviation.
     *
     * @property mean The mean (average) value of the distribution.
     * @property stddev The standard deviation, which measures the amount of variation or dispersion from the mean.
     */
    class Normal(private val mean: Long, private val stddev: Double) : Distribution {
        private val random = Random()

        /**
         * Generates a random long value normally distributed with the specified [mean] and [stddev].
         * @return A long value normally distributed based on the specified mean and standard deviation.
         */
        override fun nextValue(): Long {
            return (mean + stddev * random.nextGaussian()).toLong()
        }
    }
}