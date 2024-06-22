package org.datacraft.casters

import org.datacraft.models.Caster
import org.datacraft.SpecException
import kotlin.math.roundToInt

/**
 * Caster to round values to a specific number of digits.
 *
 * Not using SPI or @CastType, to make it easier to create multiple different round casters
 *
 * @param digits the number of digits to round to
 */
class RoundCaster(private val digits: Int = 0) : Caster<Number> {
    @Throws(SpecException::class)
    override fun cast(value: Any): Number {
        return try {
            roundValue(value.toString().toFloat())
        } catch (e: NumberFormatException) {
            throw SpecException(e.message!!)
        }
    }

    @Throws(SpecException::class)
    override fun cast(value: List<Any>): List<Number> {
        return try {
            value.map { roundValue(it.toString().toFloat()) }
        } catch (e: NumberFormatException) {
            throw SpecException(e.message!!)
        }
    }

    private fun roundValue(value: Float): Number {
        return "%.${digits}f".format(value).toDouble()
    }
}
