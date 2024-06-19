package org.datacraft.tests

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.datacraft.Registries
import org.datacraft.SpecException
import org.datacraft.ValueSupplier
import org.datacraft.models.Caster
import org.datacraft.suppliers.CastSupplier

// A simple implementation of ValueSupplier for testing
class TestValueSupplier(private val value: Any) : ValueSupplier<Any> {
    override fun next(iteration: Long): Any = value
}

class CastSupplierTest : StringSpec({

    val validTestCases = listOf(
        Triple("int", "123.45", 123),
        Triple("float", "123.45", 123.45f),
        Triple("string", 123.45, "123.45"),
        Triple("hex", "255", "ff")
    )

    validTestCases.forEachIndexed { index, (type, input, expected) ->
        "cast test case ${index + 1}: $type" {
            val caster: Caster<Any> = Registries.casterFor(type) ?: throw SpecException("Unknown caster type: $type")

            val supplier = TestValueSupplier(input)
            val castSupplier = CastSupplier(supplier, caster)
            castSupplier.next(0) shouldBe expected
        }
    }

    "invalid caster should throw SpecException" {
        shouldThrow<SpecException> {
            Registries.casterFor("invalidCasterName")
        }
    }
})
