package org.datacraft

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class SpecBuilderTest : StringSpec({

    "should build DataSpec with range field" {
        val sb = SpecBuilder()
        val spec = sb
            .field("product_id").range(0, 100, mapOf("prefix" to "prod_"))
            .build()

        spec.data.shouldNotBeNull()
    }

    "should build DataSpec with values field" {
        val sb = SpecBuilder()
        val spec = sb
            .field("product_name").values(listOf("Wireless Mouse", "Keyboard", "Monitor"))
            .build()

        spec.data.shouldNotBeNull()
    }

    "should build DataSpec with uuid field" {
        val sb = SpecBuilder()
        val spec = sb
            .field("id").uuid()
            .build()

        spec.data.shouldNotBeNull()
    }

    "should build DataSpec with date field" {
        val sb = SpecBuilder()
        val spec = sb
            .field("field").date()
            .build()

        spec.data.shouldNotBeNull()
    }

    "should build DataSpec with date.iso field" {
        val sb = SpecBuilder()
        val spec = sb
            .field("field").dateIso()
            .build()


        spec.data.shouldNotBeNull()
    }

    "should build DataSpec with date.iso.ms field" {
        val sb = SpecBuilder()
        val spec = sb
            .field("field").dateIsoMs()
            .build()

        spec.data.shouldNotBeNull()
    }

    "should build DataSpec with date.epoch field" {
        val sb = SpecBuilder()
        val spec = sb
            .field("field").dateEpoch()
            .build()

        spec.data.shouldNotBeNull()
    }

    "should build DataSpec with date.epoch.ms field" {
        val sb = SpecBuilder()
        val spec = sb
            .field("field").dateEpochMs()
            .build()

        spec.data.shouldNotBeNull()
    }

    "should build DataSpec with key defined field" {
        val sb = SpecBuilder()
        val examples = listOf(
            "customer_id:uuid",
            "ts:date.iso?prefix=date-"
        )

        for (example in examples) {
            val spec = sb
                .fieldByKey(example)
                .build()
            spec.data.shouldNotBeNull()
        }
    }

    "should build DataSpec with key defined field with data" {
        val sb = SpecBuilder()
        val spec = sb
            .fieldByKey("test:values", data = listOf("one", "two", "tre"))
            .build()
        spec.data.shouldNotBeNull()
        val records = spec.entries(1)
        records[0].containsKey("test") shouldBe true
        records[0]["test"] shouldBe "one"
    }

    "should throw SpecException when invalid format for fieldByKey" {
        shouldThrow<SpecException> {
            SpecBuilder().fieldByKey("no_colon_field?prefix=foo&cast=string")
        }
    }

    "should throw SpecException when invalid format for refByKey" {
        shouldThrow<SpecException> {
            SpecBuilder().refByKey("no_colon_field?prefix=foo&cast=string")
        }
    }

    "should build DataSpec with multiple fields" {
        val spec = SpecBuilder()
            .field("product_id").range(0, 100, mapOf("prefix" to "prod_"))
            .field("product_name").values(listOf("Wireless Mouse", "Keyboard", "Monitor"))
            .fieldByKey("customer_id:uuid")
            .ref("one").values(listOf(1, 2, 3))
            .ref("two").values(listOf("a", "b", "c"))
            .refByKey("ts:date.iso")
            .field("onetwo").combine(listOf("one", "two"))
            .build()

        spec.data.shouldNotBeNull()

        val entries = spec.entries(3)
        entries.size shouldBe 3
    }
})
