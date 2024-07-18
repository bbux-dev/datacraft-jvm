package org.datacraft

import org.datacraft.annotations.CastName
import org.datacraft.casters.RoundCaster
import org.datacraft.casters.ZFillCaster
import org.datacraft.models.Caster
import java.util.*

internal object Registries {
    private val defaultValues: Map<String, Any> = mapOf(
        "date_format" to "yyyy-MM-dd",
        "date_duration_days" to 30,
        "date_stddev_days" to 5.0,
        "outfile_prefix" to "generated",
        "outfile_extension" to "",
        "data_dir" to ".",
        "char_class_join_with" to "",
        "combine_as_list" to false,
        "combine_join_with" to ""
    )
    private val casters = mutableMapOf<String, Caster<Any>>()

    init {
        val serviceLoader = ServiceLoader.load(Caster::class.java)
        for (caster in serviceLoader) {
            val annotation = caster::class.annotations.filterIsInstance<CastName>().firstOrNull()
                ?: throw RuntimeException("Caster ${caster::class.simpleName} is missing @CastName annotation")
            casters[annotation.value] = caster as Caster<Any>
        }
        // special for round and zfill casters
        for (i in 0..10) {
            casters["round$i"] = RoundCaster(i) as Caster<Any>
            casters["zfill$i"] = ZFillCaster(i) as Caster<Any>
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getDefault(key: String): T =
        defaultValues[key] as? T ?: throw IllegalArgumentException("No default value for key: $key")

    fun casterFor(type: String): Caster<Any> {
        return casters[type] ?: throw SpecException("No caster with name $type registered")
    }
}
