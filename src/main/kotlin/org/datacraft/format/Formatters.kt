package org.datacraft.format

import org.datacraft.models.Formatter
import java.util.*

object Formatters {
    fun configuredTypes() : List<String> {
        val serviceLoader = ServiceLoader.load(Formatter::class.java)
        return serviceLoader.map { e -> e.name() }
    }

    fun forType(format: String): Formatter? {
        val serviceLoader = ServiceLoader.load(Formatter::class.java)
        return serviceLoader.firstOrNull { e -> e.name() == format }
    }
}