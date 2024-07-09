package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.shouldBe
import org.datacraft.SpecException

class GeoLongLoaderTest : StringSpec({

    "When valid spec geo.long supplier is created" {
        // given
        val loader = GeoLongLoader()
        loader.typeNames() shouldBe listOf("geo.long")
        val spec = basicFieldSpec("geo.long")
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        val value = supplier.next(1) as Double
        value shouldBeGreaterThan -180.0
        value shouldBeLessThan 180.0
    }

    "When invalid geo.long spec SpecException in thrown" {
        // given
        val loader = GeoLongLoader()
        // bad data
        val config = mapOf("start_long" to -199.0)
        val spec = basicFieldSpec("geo.long", config=config)
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader())
        }
    }
})
