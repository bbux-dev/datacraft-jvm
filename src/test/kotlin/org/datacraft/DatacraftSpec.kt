package org.datacraft
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldNotBeNull

class DatacraftSpec : StringSpec({
    "parseSpec should correctly parse a raw spec into a Map of FieldSpec" {
        val rawSpec: Map<String, Any> = mapOf("field" to Specs.values(listOf("one", "two", "tre")))
        val spec: Map<String, FieldSpec> = Datacraft.parse(rawSpec)

        spec.size shouldBe 1
    }

    "parseStringSpec should correctly parse a JSON string into a DataSpec object" {
        val json = """
            {
                "foo": {
                    "type": "values",
                    "data": ["one", "two", "tre"]
                }   
            }
        """.trimIndent()
        assert(TestUtils.validateJson(json))

        val spec: DataSpec = Datacraft.parseString(json)
        spec.shouldNotBeNull()
    }
})