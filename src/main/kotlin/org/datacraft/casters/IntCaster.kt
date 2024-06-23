package org.datacraft.casters

import org.datacraft.models.Caster
import org.datacraft.SpecException
import org.datacraft.annotations.CastName

/**
 * Caster to convert values to integers.
 */
@CastName("int")
class IntCaster : Caster<Int> {
    @Throws(SpecException::class)
    override fun cast(value: Any): Int {
        return try {
            value.toString().toFloat().toInt()
        } catch (e: NumberFormatException) {
            throw SpecException("Unable to cast $value as Int")
        }
    }

    @Throws(SpecException::class)
    override fun cast(value: List<Any>): List<Int> {
        return try {
            value.map { it.toString().toFloat().toInt() }
        } catch (e: NumberFormatException) {
            throw SpecException("Unable to cast $value as Int")
        }
    }
}
