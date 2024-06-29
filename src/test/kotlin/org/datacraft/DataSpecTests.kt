package org.datacraft
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class DataSpecTests : StringSpec({
    "parseSpec should correctly parse a raw spec into a Map of FieldSpec" {
        val sb = Datacraft.specBuilder()
        val spec = sb.field("field").values(listOf("one", "two", "tre")).build()

        spec.data.size shouldBe 1
    }

    "parseString should correctly parse a JSON string into a DataSpec object" {
        val json = """
            {
                "foo": {
                    "type": "values",
                    "data": ["one", "two", "tre"]
                }
            }
        """
        assert(TestUtils.validateJson(json))

        val spec: DataSpec = DataSpec.parseString(json)
        spec.shouldNotBeNull()
    }

    "should generate the correct number of records" {
        val fieldSpecs = mapOf(
            "id" to listOf(1, 2, 3),
            "name" to listOf("bob", "ann", "cara")
        )
        val generator = Datacraft.generator(fieldSpecs, 5)

        val results = mutableListOf<Map<String, Any?>>()
        for (record in generator) {
            results.add(record)
        }

        results shouldHaveSize 5
    }

    "should throw NoSuchElementException if no more elements" {
        val fieldSpecs =  mapOf(
            "id" to listOf(1, 2, 3)
        )

        val generator = Datacraft.generator(fieldSpecs, 1)

        // Exhaust the generator
        val first = generator.next()

        // Attempt to access beyond the end
        shouldThrow<NoSuchElementException> {
            generator.next()
        }
    }

    "each record should contain specified fields" {
        val fieldSpecs = mapOf(
            "id" to listOf(1, 2, 3),
            "name" to listOf("bob", "ann", "cara")
        )
        val generator = Datacraft.generator(fieldSpecs, 1)
        val record = generator.next()

        record.keys shouldContainAll listOf("id", "name")
    }

    "generateRecords should return correct number of populated User objects" {
        // Setup
        val fields = mapOf(
            "id" to 1,
            "name" to "Alice",
            "age" to 30
        )

        val generator = Datacraft.generateRecords(fields, 3, User::class.java)

        // Test
        val results = generator.asSequence().toList()

        // Assertions
        results.size shouldBe 3
        results shouldContainExactly listOf(
            User(1, "Alice", 30),
            User(1, "Alice", 30),
            User(1, "Alice", 30)
        )
    }

})

data class User(val id: Int, val name: String, val age: Int)