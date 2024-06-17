package org.datacraft.suppliers

import org.datacraft.Distributions
import org.datacraft.Registries
import org.datacraft.SpecException
import org.datacraft.ValueSupplier
import org.datacraft.models.Distribution
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object Dates {
    private const val SECONDS_IN_DAY = 24.0 * 60.0 * 60.0

    fun date(options: Map<String, Any>): ValueSupplier<String> {
        val hourSupplier = options["hour_supplier"] as? ValueSupplier<Int>
        if ("center_date" in options || "stddev_days" in options) {
            return createStatsBasedDateSupplier(hourSupplier, options)
        }
        return createUniformDateSupplier(hourSupplier, options)
    }

    fun epochDate(asMillis: Boolean = false, options: Map<String, Any>): ValueSupplier<Long> {
        val (distribution, _) = if ("center_date" in options || "stddev_days" in options) {
            gaussDateTimestamp(options)
        } else {
            uniformDateTimestamp(options)
        }
        return EpochDateSupplier(distribution, asMillis)
    }

    private fun createStatsBasedDateSupplier(hourSupplier: ValueSupplier<Int>?, options: Map<String, Any>): DateSupplier {
        val (distribution, dateFormat) = gaussDateTimestamp(options)
        return DateSupplier(dateFormat, distribution, hourSupplier)
    }

    private fun createUniformDateSupplier(hourSupplier: ValueSupplier<Int>?, options: Map<String, Any>): DateSupplier {
        val (distribution, dateFormat) = uniformDateTimestamp(options)
        return DateSupplier(dateFormat, distribution, hourSupplier)
    }

    private fun uniformDateTimestamp(args: Map<String, Any>): Pair<Distribution, String> {
        val durationDays = args.getOrDefault("duration_days", Registries.getDefault("date_duration_days")).toString()
        val offset = args.getOrDefault("offset", 30).toString().toInt()
        val start = args["start"]?.toString()
        val end = args["end"]?.toString()
        val dateFormat = args.getOrDefault("format", Registries.getDefault("date_format")).toString()
        val (startTs, endTs) = generateUniformTimestamps(start, end, offset, durationDays.toInt(), dateFormat)
        val timestampDistribution = Distributions.Uniform(startTs, endTs)
        return Pair(timestampDistribution, dateFormat)
    }

    /**
     * Generates a pair of Unix epoch timestamps representing a uniformly distributed range between two dates.
     *
     * @param start The starting date string.
     * @param end The ending date string.
     * @param offset The number of days to shift the starting and ending dates. Positive is backward, negative is forward.
     * @param duration The number of days from the start date to define the end of the range if `end` is not specified.
     * @param dateFormatString The pattern used for parsing the date strings.
     * @return A pair of timestamps (start, end), or (null, null) if there's an error in date parsing or logic.
     */
    private fun generateUniformTimestamps(start: String?,
                                          end: String?,
                                          offset: Int,
                                          duration: Int,
                                          dateFormatString: String): Pair<Long, Long> {
        val formatter = DateTimeFormatter.ofPattern(dateFormatString)
        val offsetDuration = java.time.Duration.ofDays(offset.toLong())

        val startDate = start?.let {
            try {
                LocalDateTime.parse(it, formatter).minus(offsetDuration)
            } catch (e: DateTimeParseException) {
                throw SpecException("Date format string does not match start date: $it", e)
            }
        } ?: LocalDateTime.now().minus(offsetDuration)

        val endDate = end?.let {
            try {
                // Adding one day to make the end date inclusive
                LocalDateTime.parse(it, formatter).plusDays(1).minus(offsetDuration)
            } catch (e: DateTimeParseException) {
                throw SpecException("Date format string does not match end date: $it", e)
            }
        } ?: startDate.plusDays(duration.toLong())

        val startTs = startDate.atZone(ZoneId.systemDefault()).toEpochSecond()
        val endTs = endDate.atZone(ZoneId.systemDefault()).toEpochSecond()

        if (endTs < startTs) {
            throw SpecException("Warning: End date ($endDate) is before start date ($startDate)")
        }

        return startTs to endTs
    }

    private fun gaussDateTimestamp(args: Map<String, Any>): Pair<Distribution, String> {
        val centerDateStr = args["center_date"].toString()
        val stddevDays = args.getOrDefault("stddev_days", Registries.getDefault("date_stddev_days")).toString().toDouble()
        val dateFormat = args.getOrDefault("format", Registries.getDefault("date_format")).toString()
        val centerDate = LocalDateTime.parse(centerDateStr, DateTimeFormatter.ofPattern(dateFormat))
        val mean = centerDate.toEpochSecond(LocalDateTime.now().atZone(ZoneId.systemDefault()).offset)
        val stddev = stddevDays * SECONDS_IN_DAY
        val distribution = Distributions.Normal(mean, stddev)
        return Pair(distribution, dateFormat)
    }
}


/**
 * Supplies formatted date strings based on a timestamp distribution and optional hour adjustments.
 *
 * @property dateFormatString The string format used to format the date values.
 * @property timestampDistribution The distribution to generate timestamp values.
 * @property hourSupplier Optional supplier to provide specific hours for the date values, allowing restriction to certain time periods.
 */
class DateSupplier(
    private val dateFormatString: String,
    private val timestampDistribution: Distribution,
    private val hourSupplier: ValueSupplier<Int>? = null
) : ValueSupplier<String> {

    private val dateFormatter = DateTimeFormatter.ofPattern(dateFormatString)

    /**
     * Provides the next formatted date string.
     *
     * @param iteration The iteration index used for any necessary calculations in value generation.
     * @return A formatted date string as per [dateFormatString].
     */
    override fun next(iteration: Long): String {
        val randomSeconds = timestampDistribution.nextValue()
        var nextDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(randomSeconds), ZoneId.systemDefault())

        hourSupplier?.let {
            val nextHour = it.next(iteration)
            nextDate = nextDate.withHour(nextHour)
        }

        return nextDate.format(dateFormatter)
    }
}

/**
 * Value Supplier implementation for epoch dates
 */
class EpochDateSupplier(private val distribution: Distribution, private val asMillis: Boolean) : ValueSupplier<Long> {
    override fun next(iteration: Long): Long {
        val randomSeconds = distribution.nextValue()
        return if (asMillis) {
            randomSeconds*1000
        } else {
            randomSeconds
        }
    }

}