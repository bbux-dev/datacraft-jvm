package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.datacraft.*

class SampleLoaderTest : StringSpec({

    "When valid spec sample supplier is created" {
        // given
        val loader = SampleLoader()
        loader.typeNames() shouldBe listOf("sample")
        val data = listOf(1, 2, 3, 5, 8, 13)
        val spec = basicFieldSpec("sample", data)
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        supplier.next(1).shouldNotBeNull()
    }

    "When invalid sample spec SpecException in thrown" {
        // given
        val loader = SampleLoader()
        // bad data
        val data = mapOf("a" to 1, "b" to 3)
        val spec = basicFieldSpec("sample", data)
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader())
        }
    }
})
