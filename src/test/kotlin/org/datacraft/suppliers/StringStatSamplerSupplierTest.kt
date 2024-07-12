package org.datacraft.suppliers

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.datacraft.ValueSupplier

class StringStatSamplerSupplierTest : StringSpec({

    "when stddev is zero, count is mean" {
        val supplier : ValueSupplier<String> = StringStatSamplerSupplier(
            data = "abc123", mean = 3.0
        )

        for (i in 1..10) {
            val value = supplier.next(i.toLong())
            value.length shouldBe 3
        }
    }

})
