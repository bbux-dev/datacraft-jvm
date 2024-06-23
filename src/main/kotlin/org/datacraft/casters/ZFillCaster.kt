package org.datacraft.casters

import org.datacraft.models.Caster
import org.datacraft.SpecException

/**
 * Caster to convert values to ZFill.
 */
class ZFillCaster(private val width: Int) : Caster<Any> {
    override fun cast(value: Any): Any {
        val valueString = value.toString()
        return valueString.padStart(width + valueString.length, '0')
    }

    override fun cast(value: List<Any>): List<Any> {
        return value.map { cast(it) }
    }
}