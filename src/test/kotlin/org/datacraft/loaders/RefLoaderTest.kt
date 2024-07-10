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
        val spec = FieldSpec.forType("ref", mapOf("ref" to ref))
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
        val spec = FieldSpec.forType("ref", mapOf("data" to ref))
        // when
        val supplier = loader.load(spec, testLoader(specWithRef))
        // then
        supplier.next(1).shouldNotBeNull()
    }

    "When invalid ref spec SpecException in thrown" {
        shouldThrow<SpecException> {
            // missing ref or data definition
            FieldSpec.forType("ref", mapOf())
        }
    }
})
