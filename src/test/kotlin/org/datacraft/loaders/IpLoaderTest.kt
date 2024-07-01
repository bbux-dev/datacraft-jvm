package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.datacraft.*

class IpLoaderTest : StringSpec({

    "When valid spec ipv4 supplier is created" {
        // given
        val loader = IpLoader()
        loader.typeNames() shouldBe listOf("ip", "ipv4")
        val spec = basicFieldSpec("ipv4")
        // when
        val supplier = loader.load(spec, testLoader())
        // then
        supplier.next(1).shouldNotBeNull()
    }

    "When invalid ipv4 spec SpecException in thrown" {
        // given
        val loader = IpLoader()
        // bad data
        val config = mapOf("cidr" to "a.b.c.d/44")
        val spec = basicFieldSpec("ipv4", data=null, config=config)
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader())
        }
    }
})
