package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.datacraft.*

class RefLoaderTest : StringSpec({

    "When valid spec ref supplier is created" {
        // given
        val loader = RefLoader()
        val specWithRef = """
            {
              "refs": {
                "foo": "bar"
              }
            }
            """.trimIndent()
        loader.typeNames() shouldBe listOf("ref")
        val ref = "foo"
        val spec = basicFieldSpec("ref", ref = ref)
        // when
        val supplier = loader.load(spec, testLoader(specWithRef))
        // then
        supplier.next(1).shouldNotBeNull()
    }

    "When valid spec use data as ref supplier is created" {
        // given
        val loader = RefLoader()
        val specWithRef = """
            {
              "refs": {
                "foo": "bar"
              }
            }
            """.trimIndent()
        loader.typeNames() shouldBe listOf("ref")
        val ref = "foo"
        val spec = basicFieldSpec("ref", data = ref)
        // when
        val supplier = loader.load(spec, testLoader(specWithRef))
        // then
        supplier.next(1).shouldNotBeNull()
    }

    "When invalid ref spec SpecException in thrown" {
        // given
        val loader = RefLoader()
        // missing ref or data definition
        val spec = basicFieldSpec("ref")
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader())
        }
    }
})
