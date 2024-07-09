package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.datacraft.*

class GeoPairLoaderTest : StringSpec({

    "When valid spec geo.pair supplier is created" {
        // given
        val loader = GeoPairLoader()
        loader.typeNames() shouldBe listOf("geo.pair")
        val spec = basicFieldSpec("geo.pair")
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        supplier.next(1).shouldNotBeNull()
    }

    "When valid spec geo.pair and as_list=true values are lists with two elements" {
        // given
        val loader = GeoPairLoader()
        loader.typeNames() shouldBe listOf("geo.pair")
        val config = mapOf("as_list" to true)
        val spec = basicFieldSpec("geo.pair", config=config)
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        val value = supplier.next(1)
        (value is List<*>) shouldBe true
        (value as List<*>).size shouldBe 2
    }

    "When invalid geo.pair spec SpecException in thrown" {
        // given
        val loader = GeoPairLoader()
        // bad data
        val config = mapOf("start_long" to -199.0)
        val spec = basicFieldSpec("geo.pair", config=config)
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader())
        }
    }
})
