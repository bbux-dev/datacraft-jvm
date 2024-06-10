package org.datacraft

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SuppliersTest : StringSpec({
    "values should correctly supply a sequence based on the given list" {
        val data = listOf("one", "two", "tre", "for", "fyv")
        val supplier: ValueSupplier<Any> = Suppliers.values(data)

        assertSoftly {
            supplier.next(1) shouldBe "one"
            supplier.next(2) shouldBe "two"
            supplier.next(3) shouldBe "tre"
            supplier.next(4) shouldBe "for"
            supplier.next(5) shouldBe "fyv"
            supplier.next(6) shouldBe "one"
        }
    }

    "combine should correctly concatenate values" {
        val first = listOf("one", "two", "tre", "for", "fyv")
        val second = listOf(1, 2, 3, 4)
        val suppliers = listOf(
            Suppliers.values(first),
            Suppliers.values(second)
        )
        val supplier = Suppliers.combine(suppliers);
        assertSoftly {
            supplier.next(1) shouldBe "one1"
            supplier.next(2) shouldBe "two2"
            supplier.next(3) shouldBe "tre3"
            supplier.next(4) shouldBe "for4"
            supplier.next(5) shouldBe "fyv1"
            supplier.next(6) shouldBe "one2"
        }

    }
})
