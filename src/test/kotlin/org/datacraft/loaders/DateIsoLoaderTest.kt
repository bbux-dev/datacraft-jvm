package org.datacraft.loaders

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.datacraft.loaders.dates.DateIsoLoader
import org.datacraft.loaders.dates.DateIsoMicrosLoader
import org.datacraft.loaders.dates.DateIsoMillisLoader

class DateIsoLoaderTest : StringSpec({

    "When valid spec date.iso supplier is created" {
        // given
        val loader = DateIsoLoader()
        loader.typeNames() shouldBe listOf("date.iso")
        val spec = basicFieldSpec("date.iso")
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        supplier.next(1).shouldNotBeNull()
    }

    "When valid spec date.iso.ms supplier is created" {
        // given
        val loader = DateIsoMillisLoader()
        loader.typeNames() shouldBe listOf("date.iso.millis", "date.iso.ms")
        val spec = basicFieldSpec("date.iso.millis")
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        supplier.next(1).shouldNotBeNull()
    }


    "When valid spec date.iso.micros supplier is created" {
        // given
        val loader = DateIsoMicrosLoader()
        loader.typeNames() shouldBe listOf("date.iso.micros", "date.iso.us")
        val spec = basicFieldSpec("date.iso.micros")
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        supplier.next(1).shouldNotBeNull()
    }
})
