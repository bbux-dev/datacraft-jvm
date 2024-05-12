package org.datacraft.suppliers
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.datacraft.Suppliers
import org.datacraft.ValueSupplier

class CombineSupplierTest : StringSpec({
    "combines values into a string without delimiter when joinWith is null" {
        val suppliers = helloWorldSuppliers()
        val result = Suppliers.combine(suppliers).next(1)
        result shouldBe "HelloWorld"
    }

    "combines values with a delimiter when joinWith is specified" {
        val suppliers = helloWorldSuppliers()
        val result = Suppliers.combine(suppliers, ", ").next(1)
        result shouldBe "Hello, World"
    }

    "returns a list of values when asList is true" {
        val suppliers = helloWorldSuppliers()
        val result = Suppliers.combine(suppliers, asList = true).next(1)
        result shouldBe listOf("Hello", "World")
    }

    "handles empty list of suppliers" {
        val suppliers = emptyList<ValueSupplier<Any>>()
        val result = Suppliers.combine(suppliers).next(1)
        result shouldBe ""
    }
})

private fun helloWorldSuppliers() : List<ValueSupplier<Any>> {
    return listOf(
        valueSupplier("Hello"),
        valueSupplier("World")
    )
}
private fun valueSupplier(value: String) = object : ValueSupplier<Any> {
    override fun next(iteration: Long) = value
}

