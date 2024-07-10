package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldBeOneOf
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.datacraft.*

class WeightedRefLoaderTest : StringSpec({

    "When valid spec weighted_ref supplier is created" {
        // given
        val loader = WeightedRefLoader()
        loader.typeNames() shouldBe listOf("weighted_ref")
        val specWithRefs = """
            {
              "refs": {
                "foo": 42,
                "bar": 77
              }
            }
        """.trimIndent()
        val data = mapOf("foo" to 0.5, "bar" to 0.5)
        val spec = basicFieldSpec("weighted_ref", data)
        // when
        val supplier = loader.load(spec, testLoader(specWithRefs))
        // then
        val value = supplier.next(1)
        value shouldBeIn listOf(42.0, 77.0)
    }

    "When invalid weighted_ref spec SpecException in thrown" {
        // given
        val loader = WeightedRefLoader()
        // bad data
        val data = listOf(1, 2, 3, 4)
        val spec = basicFieldSpec("weighted_ref", data = data)
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader())
        }
    }
})
