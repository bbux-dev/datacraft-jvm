package org.datacraft.casters

import org.datacraft.models.Caster
import org.datacraft.SpecException
import org.datacraft.annotations.CastName

/**
 * Caster to convert values to hexadecimal strings.
 */
@CastName("hex")
class HexCaster : Caster<String> {
    @Throws(SpecException::class)
    override fun cast(value: Any): String {
        return try {
            value.toString().toFloat().toInt().toString(16)
        } catch (e: NumberFormatException) {
            throw SpecException("Unable to cast $value as hex")
        }
    }

    @Throws(SpecException::class)
    override fun cast(value: List<Any>): List<String> {
        return try {
            value.map { it.toString().toFloat().toInt().toString(16) }
        } catch (e: NumberFormatException) {
            throw SpecException("Unable to cast $value as hex")
        }
    }
}
