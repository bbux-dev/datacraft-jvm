package org.datacraft.models

import org.datacraft.FieldSpec
import org.datacraft.Loader
import org.datacraft.ValueSupplier

/**
 * Service Provider Interface for loading a ValueSupplier for a specific type.
 *
 * Example:
 * ```
 *class DateEpochMillisLoader : ValueSupplierLoader<Long> {
 *     override fun typeNames(): List<String> = listOf("date.epoch.ms", "date.epoch.millis")
 *
 *     override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<Long> {
 *         val updatedConfig: MutableMap<String, Any> = spec.config?.toMutableMap() ?: mutableMapOf()
 *         return Dates.epochDate(asMillis = true, updatedConfig)
 *     }
 * }
 * ```
 */
interface ValueSupplierLoader<T> {
    fun typeNames() : List<String>
    fun load(spec: FieldSpec, loader: Loader): ValueSupplier<T>
}