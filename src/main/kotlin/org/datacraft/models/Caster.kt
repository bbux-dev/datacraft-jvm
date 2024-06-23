package org.datacraft.models

/**
 * Interface for casting values to a specific type.
 *
 * @param T the type to cast to
 */
interface Caster<T> {
    /**
     * Casts a single value to the specified type.
     *
     * @param value the value to cast
     * @return the cast value
     * @throws SpecException if the value cannot be cast
     */
    fun cast(value: Any): T

    /**
     * Casts a list of values to the specified type.
     *
     * @param value the list of values to cast
     * @return the list of cast values
     * @throws SpecException if any value in the list cannot be cast
     */
    fun cast(value: List<Any>): List<T>
}
