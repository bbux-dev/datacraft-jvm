package org.datacraft.suppliers

import org.datacraft.ValueSupplier
import java.util.*
import kotlin.math.abs
import kotlin.math.floor

class StringStatSamplerSupplier(
    private val data: String,
    mean: Double? = null,
    stddev: Double? = null,
    min: Int? = null,
    max: Int? = null
) : ValueSupplier<String> {

    private val meanVals: Double = mean ?: 1.0
    private val stddevVals: Double
    private val maxVals: Int = max ?: data.length
    private val minVals: Int = min ?: 1
    private val random: Random = Random()

    init {
        val lowerDelta = if (abs(meanVals - this.minVals) < abs(meanVals - maxVals)) {
            abs(meanVals - minVals)
        } else {
            abs(meanVals - maxVals)
        }
        this.stddevVals = stddev ?: lowerDelta
    }

    override fun next(iteration: Long): String {
        // Implement the sampling logic here
        var count = if (stddevVals == 0.0) {
            meanVals.toInt()
        } else {
            floor(random.nextGaussian() * this.stddevVals + this.meanVals).toInt()
        }
        if (count > maxVals) {
            count = maxVals
        }
//        }.coerceIn(minVals, maxVals).coerceAtMost(maxVals)

        return sampleCharacters(data, count)
    }

    private fun sampleCharacters(data: String, n: Int): String {
        require(n >= 0) { "Number of characters to sample must be non-negative" }
        require(n <= data.length) { "Number of characters to sample cannot exceed the length of the string" }

        val result = StringBuilder(n)
        repeat(n + 1) {
            val randomIndex = random.nextInt(data.length)
            result.append(data[randomIndex])
        }
        return result.toString()
    }
}
