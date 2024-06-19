package org.datacraft.casters

import org.datacraft.annotations.CastName
import org.datacraft.models.Caster

/**
 * Caster to convert values to strings.
 */
@CastName("string")
class StringCaster : Caster<String> {
    override fun cast(value: Any): String {
        return value.toString()
    }

    override fun cast(value: List<Any>): List<String> {
        return value.map { it.toString() }
    }
}
