package org.datacraft.casters

import org.datacraft.annotations.CastName
import org.datacraft.models.Caster

/**
 * Caster to trim leading and trailing whitespace from values as strings.
 */
@CastName("trim")
class TrimCaster : Caster<String> {
    override fun cast(value: Any): String {
        return value.toString().trim()
    }

    override fun cast(value: List<Any>): List<String> {
        return value.map { it.toString().trim() }
    }
}
