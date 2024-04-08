package org.datacraft

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SuppliersTest : StringSpec({
    "values should correctly supply a sequence based on the given list" {
        val fieldSpec: FieldSpec = FieldSpec.values(listOf("one", "two", "tre", "for", "fyv"))
        val supplier: ValueSupplier<Any> = Suppliers.values(fieldSpec)

        supplier.next(0) shouldBe "one"
        supplier.next(1) shouldBe "two"
        supplier.next(2) shouldBe "tre"
        supplier.next(3) shouldBe "for"
        supplier.next(4) shouldBe "fyv"
        supplier.next(5) shouldBe "one"
    }
})
