package org.datacraft

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot


class LoaderTest : StringSpec({
    "When init then date type should be discovered" {
        val json = """
            {"foo": {"type": "date"}}
        """
        assert(TestUtils.validateJson(json))

        val supplier = supplierForSpec(json, "foo")
        supplier shouldNot beNull()
        supplier.next(1) shouldNot beNull()
    }
    "When init then combine type should be discovered" {
        val json = """
            {
              "foo": {"type": "combine", "refs": ["one", "two"]},
              "refs": {
                "one": ["alpha", "beta"],
                "two": [1, 2]
              }
            }
        """
        assert(TestUtils.validateJson(json))

        val supplier = supplierForSpec(json, "foo")
        supplier shouldNot beNull()
        supplier.next(1) shouldNot beNull()
    }
    "When init then uuid type should be discovered" {
        val json = """
            {
              "foo:uuid": {}
            }
        """
        assert(TestUtils.validateJson(json))

        val supplier = supplierForSpec(json, "foo")
        supplier shouldNot beNull()
        supplier.next(1) shouldNot beNull()
    }
    "When init then sample type should be discovered" {
        val json = """
            {
              "foo:sample": [1, 2, 3, 5, 8, 13]
            }
        """
        assert(TestUtils.validateJson(json))

        val supplier = supplierForSpec(json, "foo")
        supplier shouldNot beNull()
        supplier.next(1) shouldNot beNull()
    }
    "When spec configured with cast and prefix value should be correct" {
        val json = """
            {
              "foo": {
                "type": "values",
                "data": 1.23456,
                "config": {
                  "cast": "int",
                  "prefix": "num"
                }
              }
            }
        """
        assert(TestUtils.validateJson(json))

        val supplier = supplierForSpec(json, "foo")
        supplier.next(1) shouldBe "num1"
    }
    "When configured with multiple casts, order is respected" {
        val json = """
            {
              "bar": {
                "type": "values",
                "data": 255.456,
                "config": {
                  "cast": "int;hex"
                }
              }
            }
        """
        assert(TestUtils.validateJson(json))

        val supplier = supplierForSpec(json, "bar")
        supplier.next(1) shouldBe "ff"
    }
})

internal fun supplierForSpec(json: String, key: String): ValueSupplier<Any> {
    val spec: DataSpec = DataSpec.parseString(json)
    val loader = Loaders.init(spec)
    val supplier = loader.get(key)
    return supplier
}