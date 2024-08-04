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

    "should generate typed data records from map" {
        val data = mapOf(
            "name" to listOf("cat", "dog", "pig", "frog"),
            "count" to mapOf(
                "1" to 0.5, "2" to 0.3, "3" to 0.1, "4" to 0.1
            )
        )
        val iterator = Datacraft.generateRecords(data, 4, SampleRecord::class.java)

        val records = iterator.asSequence().toList()
        records.size shouldBe 4
        val first = records[0]
        first.name shouldBe "cat"
        first.count shouldBeInRange 1..4
    }
    "should generate entries from JSON string" {
        val json = """
        {
          "id": { "type": "uuid" },
          "ts:date.iso": {}
        }
        """
        val entries = Datacraft.entries(json, 5)

        entries.size shouldBe 5
        entries.forEach {
            val record = it
            record["id"].shouldNotBeNull()
            record["ts"].shouldNotBeNull()
        }
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
    "should generate typed data record entries from JSON" {
        val json = """{
            "name": ["cat", "dog", "pig", "frog"],
            "count": {"1": 0.5, "2": 0.3, "3": 0.1, "4": 0.1}
        }"""
        val records = Datacraft.recordEntries(json, 4, SampleRecord::class.java)

        records.size shouldBe 4
        val first = records[0]
        first.name shouldBe "cat"
        first.count shouldBeInRange 1..4
    }
    "should generate typed data record entries from map" {
        val data = mapOf(
            "name" to listOf("cat", "dog", "pig", "frog"),
            "count" to mapOf(
                "1" to 0.5, "2" to 0.3, "3" to 0.1, "4" to 0.1
            )
        )
        val records = Datacraft.recordEntries(data, 4, SampleRecord::class.java)

        records.size shouldBe 4
        val last = records[3]
        last.name shouldBe "frog"
        last.count shouldBeInRange 1..4
    }
    "should return different fields when field groups are specified" {
        val json = """
        {
          "foo": { "type": "uuid" },
          "bar": { "type": "uuid" },
          "baz": { "type": "uuid" },
          "field_groups": [
            ["foo", "bar"],
            ["bar", "baz"]
          ]
        }
        """
        val entries = Datacraft.entries(json, 4)

        entries.size shouldBe 4

        val first = entries[0]
        first.size shouldBe 2
        first.containsKey("foo") shouldBe true
        first.containsKey("bar") shouldBe true

        val second = entries[1]
        second.size shouldBe 2
        second.containsKey("bar") shouldBe true
        second.containsKey("baz") shouldBe true
    }
})