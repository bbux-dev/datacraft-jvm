package org.datacraft

/**
 * Interface for supplying values of any type. Implementations define how values are generated or retrieved.
 *
 * Example usage:
 *```
 *class TwosValueSupplier : ValueSupplier<Int> {
 *    override fun next(iteration: Long): Int {
 *        require(iteration > 0) { "Iteration must be greater than 0" }
 *        return iteration.toInt() * 2
 *    }
 *}
 *  *
 *fun main() {
 *    val supplier: ValueSupplier<Int> = IntValueSupplier()
 *    println(supplier.next(1)) // Outputs: 2
 *    println(supplier.next(2)) // Outputs: 4
 *}
 *```
 */
interface ValueSupplier<T> {
    /**
     * Returns a value based on the given iteration.
     * @param iteration The current iteration count. 1 based indexing.
     * @return A value of type [T].
     */
    fun next(iteration: Long): T
}