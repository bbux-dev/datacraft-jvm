package org.datacraft.loaders

import org.datacraft.SpecException

internal class Common

inline fun <reified T> getConfigValue(config: Map<String, Any>, key: String, defaultValue: T): T {
    val value = config.getOrDefault(key, defaultValue)
    return try {
        value as T
    } catch (e: ClassCastException) {
        throw SpecException("Invalid type for key '$key': expected ${T::class}, found ${value!!::class}")
    }
}

internal fun gitConfigAsDouble(config: Map<String, Any>, key: String): Double? {
    val value = config[key] ?: return null
    return when (value) {
        is Double -> value
        is Number -> value.toDouble()
        else -> throw SpecException("Invalid type for key '$key': expected Double or Int, found ${value::class}")
    }
}

internal fun gitConfigAsInt(config: Map<String, Any>, key: String): Int? {
    val value = config[key] ?: return null
    return when (value) {
        is Number -> value.toInt()
        else -> throw SpecException("Invalid type for key '$key': expected Int, found ${value::class}")
    }
}




