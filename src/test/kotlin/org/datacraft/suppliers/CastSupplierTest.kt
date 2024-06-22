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
        Triple("hex", "255", "ff"),
        Triple("lower", "dAtAcRaFt", "datacraft"),
        Triple("upper", "dAtAcRaFt", "DATACRAFT"),
        Triple("trim", " datacraft ", "datacraft"),
        Triple("round0", 7.7654321, 8.0),
        Triple("round1", 7.7654321, 7.8),
        Triple("round2", 7.7654321, 7.77),
        Triple("round3", 7.7654321, 7.765),
        Triple("round4", 7.7654321, 7.7654),
        Triple("round5", 7.7654321, 7.76543),
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
