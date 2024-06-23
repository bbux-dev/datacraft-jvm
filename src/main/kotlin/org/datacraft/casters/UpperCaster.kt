package org.datacraft.casters

import org.datacraft.annotations.CastName
import org.datacraft.models.Caster
import java.util.*

/**
 * Caster to convert values to upper case strings.
 */
@CastName("upper")
class UpperCaster : Caster<String> {
    override fun cast(value: Any): String {
        return value.toString().uppercase(Locale.getDefault())
    }

    override fun cast(value: List<Any>): List<String> {
        return value.map { it.toString().uppercase(Locale.getDefault()) }
    }
}
