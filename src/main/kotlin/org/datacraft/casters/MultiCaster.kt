package org.datacraft.casters

import org.datacraft.models.Caster
import org.datacraft.SpecException

/**
 * Caster to wrap multiple casters
 */
class MultiCaster(private val casters: List<Caster<Any>>) : Caster<Any> {
    override fun cast(value: Any): Any {
        var result = value
        for (caster in casters) {
            result = caster.cast(result)
        }
        return result
    }

    override fun cast(value: List<Any>): List<Any> {
        return value.map { cast(it) }
    }
}