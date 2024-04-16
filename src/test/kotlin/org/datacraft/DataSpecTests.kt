package org.datacraft
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class DataSpecTests : StringSpec({
    "parseSpec should correctly parse a raw spec into a Map of FieldSpec" {
        val rawSpec: Map<String, Any> = mapOf("field" to Specs.values(listOf("one", "two", "tre")))
        val spec: Map<String, FieldSpec> = DataSpec.parseRaw(rawSpec)

        spec.size shouldBe 1
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
        val dataSpec = DataSpec.parse(fieldSpecs)
        val generator = dataSpec.generator(5)

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
        val dataSpec = DataSpec.parse(fieldSpecs)
        val generator = dataSpec.generator(1)

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
        val dataSpec = DataSpec.parse(fieldSpecs)
        val generator = dataSpec.generator(1)
        val record = generator.next()

        record.keys shouldContainAll listOf("id", "name")
    }
})