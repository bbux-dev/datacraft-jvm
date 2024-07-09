package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.types.shouldBeInstanceOf
import org.datacraft.*

class RandomIntRangeLoaderTest : StringSpec({

    "When valid spec rand_int_range supplier is created" {
        // given
        val loader = RandomIntRangeLoader()
        loader.typeNames() shouldBe listOf("rand_int_range")
        val data = listOf(10, 60)
        val spec = basicFieldSpec("rand_int_range", data)
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        val next = supplier.next(1)
        next.shouldBeInstanceOf<Int>()
        next shouldBeInRange  10..60
    }

    "When single data element, 0  is lower end of range" {
        // given
        val loader = RandomIntRangeLoader()
        loader.typeNames() shouldBe listOf("rand_int_range")
        val data = listOf(10)
        val spec = basicFieldSpec("rand_int_range", data)
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        val next = supplier.next(1)
        next.shouldBeInstanceOf<Int>()
        next shouldBeInRange  0..10
    }

    "When invalid rand_int_range spec SpecException in thrown" {
        // given
        val loader = RandomIntRangeLoader()
        // bad data
        val data = mapOf("a" to 1, "b" to 3)
        val spec = basicFieldSpec("rand_int_range", data)
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader())
        }
    }
})
