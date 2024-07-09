package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.shouldBe
import org.datacraft.SpecException

class GeoLatLoaderTest : StringSpec({

    "When valid spec geo.lat supplier is created" {
        // given
        val loader = GeoLatLoader()
        loader.typeNames() shouldBe listOf("geo.lat")
        val spec = basicFieldSpec("geo.lat")
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        val value = supplier.next(1) as Double
        value shouldBeGreaterThan -90.0
        value shouldBeLessThan 90.0
    }

    "When invalid geo.lat spec SpecException in thrown" {
        // given
        val loader = GeoLatLoader()
        // bad data
        val config = mapOf("start_lat" to -99.0)
        val spec = basicFieldSpec("geo.lat", config=config)
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader())
        }
    }
})
