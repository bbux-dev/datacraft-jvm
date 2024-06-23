package org.datacraft.casters

import org.datacraft.annotations.CastName
import org.datacraft.models.Caster
import java.util.*

/**
 * Caster to convert values to lowercase strings.
 */
@CastName("lower")
class LowerCaster : Caster<String> {
    override fun cast(value: Any): String {
        return value.toString().lowercase(Locale.getDefault())
    }

    override fun cast(value: List<Any>): List<String> {
        return value.map { it.toString().lowercase(Locale.getDefault()) }
    }
}
