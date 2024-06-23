package org.datacraft.suppliers

import org.datacraft.ValueSupplier
import org.datacraft.models.Caster

class  CastSupplier<T>(
    private val supplier: ValueSupplier<Any>,
    private val caster : Caster<T>) : ValueSupplier<T> {
    override fun next(iteration: Long): T {
        return caster.cast(supplier.next(iteration))
    }
}