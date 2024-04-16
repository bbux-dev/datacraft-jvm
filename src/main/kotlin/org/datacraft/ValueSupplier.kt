package org.datacraft

/**
 * Interface for supplying values of any type. Implementations define how values are generated or retrieved.
 */
interface ValueSupplier<T> {
    /**
     * Returns a value based on the given iteration.
     * @param iteration The current iteration count.
     * @return A value of type [T].
     */
    fun next(iteration: Long): T
}