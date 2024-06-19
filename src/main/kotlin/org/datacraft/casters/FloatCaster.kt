package org.datacraft.casters

import org.datacraft.models.Caster
import org.datacraft.SpecException
import org.datacraft.annotations.CastName

/**
 * Caster to convert values to floating point numbers.
 */
@CastName("float")
class FloatCaster : Caster<Float> {
    @Throws(SpecException::class)
    override fun cast(value: Any): Float {
        return try {
            value.toString().toFloat()
        } catch (e: NumberFormatException) {
            throw SpecException("Unable to cast $value as Float")
        }
    }

    @Throws(SpecException::class)
    override fun cast(value: List<Any>): List<Float> {
        return try {
            value.map { it.toString().toFloat() }
        } catch (e: NumberFormatException) {
            throw SpecException("Unable to cast $value as Floats")
        }
    }
}
