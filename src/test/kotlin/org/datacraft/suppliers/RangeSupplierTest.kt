package org.datacraft.suppliers

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RangeSupplierTest : StringSpec({
    "range supplier rotates through values in sequence" {
        val supplier = RangeSupplier(1, 3)
        supplier.next(0) shouldBe 1
        supplier.next(1) shouldBe 2
        supplier.next(2) shouldBe 3
        supplier.next(3) shouldBe 1
    }
})
