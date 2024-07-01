package org.datacraft.suppliers

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch


val ipv4Pattern = Regex(
    "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
)

class IpSupplierTest : StringSpec({
    "when no cidr supplied ip address should be valid" {
        // given
        val supplier = IpSupplier()
        // when
        val value = supplier.next(1)
        // then
        // Validate length
        value.length shouldBeInRange 7..15
        // Validate IPv4 format
        value shouldMatch ipv4Pattern
    }
    "when cidr supplied ip address should be valid" {
        // given
        val supplier = IpSupplier("10.0.0.0/16")
        // when
        val value = supplier.next(1)
        // then
        // Validate length
        value.length shouldBeInRange 7..15
        // Validate IPv4 format
        value shouldMatch ipv4Pattern
    }
})
