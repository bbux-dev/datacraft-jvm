package org.datacraft.suppliers

import org.datacraft.ValueSupplier
import java.util.*
import kotlin.math.abs
import kotlin.math.floor

class StringSamplerSupplier(
    private val data: String,
    private val countSupplier: ValueSupplier<Int>
) : ValueSupplier<String> {

    val random: Random = Random()


    override fun next(iteration: Long): String {
        // Implement the sampling logic here
        var count = countSupplier.next(iteration)
        return sampleCharacters(data, count)
    }

    private fun sampleCharacters(data: String, n: Int): String {
        require(n >= 0) { "Number of characters to sample must be non-negative" }
        require(n <= data.length) { "Number of characters to sample cannot exceed the length of the string" }

        val result = StringBuilder(n)
        repeat(n) {
            val randomIndex = random.nextInt(data.length)
            result.append(data[randomIndex])
        }
        return result.toString()
    }
}


internal class StatsCountSupplier(
    size: Int,
    mean: Double? = null,
    stddev: Double? = null,
    min: Int? = null,
    max: Int? = null
) : ValueSupplier<Int> {

    private val meanVals: Double = mean ?: 1.0
    private val stddevVals: Double
    private val maxVals: Int = max ?: size
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

    override fun next(iteration: Long): Int {
        var count = if (stddevVals == 0.0) {
            meanVals.toInt()
        } else {
            floor(random.nextGaussian() * this.stddevVals + this.meanVals).toInt()
        }
        if (count > maxVals) {
            count = maxVals
        }
        if (count < minVals) {
            count = maxVals
        }

        return count
    }
}