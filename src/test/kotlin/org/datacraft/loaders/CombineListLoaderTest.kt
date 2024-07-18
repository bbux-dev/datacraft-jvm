package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.datacraft.*

class CombineListLoaderTest : StringSpec({

    "When valid spec combine-list supplier is created" {
        // given
        val loader = CombineListLoader()
        loader.typeNames() shouldBe listOf("combine-list")
        val refs = listOf(
            listOf("first", "last"),
            listOf("first", "middle", "last")
        )
        val spec = CombineListFieldSpec(null, refs)
        // when
        val supplier = loader.load(spec, testLoader("""
            {
              "refs": {
                "first": ["bob", "ann", "carl" ],
                "middle": ["joe", "joey", "joann"],
                "last": ["smith", "jones", "johnson"]
              }
            }
        """.trimIndent()))
        // then
        supplier.next(1).shouldNotBeNull()
    }

    "When invalid combine-list spec SpecException in thrown" {
        // given
        val loader = CombineListLoader()
        // bad data
        val rawSpec = mapOf("refs" to listOf("a", "b", "c"))
        // when, then
        shouldThrow<SpecException> {
            FieldSpec.forType("combine-list", rawSpec)
        }
    }
})
