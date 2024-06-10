package org.datacraft

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

data class SampleRecord(val name: String, val count: Int)

class DatacraftTest : StringSpec({

    "should generate data records from JSON" {
        val json = """{
            "ts:date.iso": {},
            "names": ["bob", "joe", "bobby-joe"]
        }"""
        val iterator = Datacraft.generator(json, 2)

        val records = iterator.asSequence().toList()
        records.size shouldBe 2
    }
    "should generate data records from Map" {
        val map = mapOf(
            "ts" to mapOf(
                "type" to "date.iso"
            ),
            "name" to listOf("gru", "kevin", "agnus")
        )
        val iterator = Datacraft.generator(map, 3)

        val records = iterator.asSequence().toList()
        records.size shouldBe 3
        records.forEach {
            val record = it
            record["ts"].shouldNotBeNull()
            record["name"].shouldNotBeNull()
        }
    }
    "should generate typed data records from JSON" {
        val json = """{
            "name": ["cat", "dog", "pig", "frog"],
            "count": {"1": 0.5, "2": 0.3, "3": 0.1, "4": 0.1}
        }"""
        val iterator = Datacraft.generateRecords(json, 4, SampleRecord::class.java)

        val records = iterator.asSequence().toList()
        records.size shouldBe 4
        val first = records[0]
        first.name shouldBe "cat"
        first.count shouldBeInRange 1..4
    }
    "should generate entries from Map" {
        val map = mapOf(
            "ts" to mapOf(
                "type" to "date.iso"
            ),
            "name" to listOf("gru", "kevin", "agnus")
        )
        val entries = Datacraft.entries(map, 5)

        entries.size shouldBe 5
        entries.forEach {
            val record = it
            record["ts"].shouldNotBeNull()
            record["name"].shouldNotBeNull()
        }
    }
})