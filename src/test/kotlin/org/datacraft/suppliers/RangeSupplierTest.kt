package org.datacraft.suppliers

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RangeSupplierTest : StringSpec({
    "range supplier rotates through values in sequence" {
        val supplier = RangeSupplier(1, 3, 1)
        assertSoftly {
            supplier.next(0) shouldBe 1
            supplier.next(1) shouldBe 2
            supplier.next(2) shouldBe 3
            supplier.next(3) shouldBe 1
        }
    }
    "range supplier rotates through values in sequence with step" {
        val supplier = RangeSupplier(0, 5, 2)
        assertSoftly {
            supplier.next(0) shouldBe 0
            supplier.next(1) shouldBe 2
            supplier.next(2) shouldBe 4
            supplier.next(3) shouldBe 0
        }
    }
})
