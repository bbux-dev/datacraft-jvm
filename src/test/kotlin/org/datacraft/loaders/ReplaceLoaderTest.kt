package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.datacraft.*

class ReplaceLoaderTest : StringSpec({

    "When valid spec replace supplier is created" {
        // given
        val loader = ReplaceLoader()
        loader.typeNames() shouldBe listOf("replace")

        val data = mapOf("@$$" to listOf(" Luck", " Day"))
        val ref = "foo"
        val spec = ReplaceFieldSpec(null, ref, data)
        // when
        val supplier = loader.load(spec, testLoader("""{"foo": ["Bad@$$"]}"""))
        // then
        supplier.next(1).shouldNotBeNull()
        supplier.next(2) shouldBe "Bad Day"
    }

    "When invalid replace spec SpecException in thrown" {
        // given
        val loader = ReplaceLoader()
        // bad data
        val data = listOf("rice", "carrots", "peas")
        val ref = "foo"
        val spec = ReplaceFieldSpec(null, ref, data)
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader("""{"foo": ["Bad@$$"]}"""))
        }
    }
})
