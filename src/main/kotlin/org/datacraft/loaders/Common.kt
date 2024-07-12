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
    return try {
        value as Double
    } catch (e: ClassCastException) {
        throw SpecException("Invalid type for key '$key': expected ${Double::class}, found ${value::class}")
    }
}

