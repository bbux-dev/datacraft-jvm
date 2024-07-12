import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import io.kotest.matchers.types.shouldBeInstanceOf
import org.datacraft.DataSpec
import org.datacraft.SpecBuilder
import org.datacraft.loaders.CharClassMappings.CLASS_MAPPING


class CharClassSpecTests : StringSpec({


    "test char class special exclude" {
        val exclude = "&?!."
        val config = mapOf("min" to 1, "max" to 5, "exclude" to exclude)
        val spec = SpecBuilder().apply {
            field("name").charClass("special", config)
        }.build()

        val values = valuesForKey(spec, "name", 10)
        verifyValues(values, 1, 5, exclude)
    }

    "test char class word" {
        val config = mapOf("count" to 4)
        val spec = SpecBuilder().apply {
            field("name").charClass("special", config)
        }.build()

        val values = valuesForKey(spec, "name", 10)
        verifyValues(values, 4, 4)
    }

    "test char class stats config" {
        val config = mapOf("mean" to 5, "stddev" to 2, "min" to 3, "max" to 8)
        val spec = SpecBuilder().apply {
            field("name").charClass("word", config)
        }.build()

        val values = valuesForKey(spec, "name", 10)
        verifyValues(values, 3, 8)
    }

    "test char class printable" {
        val config = mapOf("mean" to 3, "stddev" to 2, "min" to 1, "max" to 5)
        val spec = SpecBuilder().apply {
            field("name").charClassAbbrev("printable", config)
        }.build()

        val values = valuesForKey(spec, "name", 10)
        verifyValues(values, 1, 5)
    }

    "test char class abbreviations" {
        val abbreviations = CLASS_MAPPING.keys.map { "cc-$it" }

        abbreviations.forEach { abbreviation ->
            val spec = SpecBuilder().apply {
                val config = mapOf("count" to 7)
                field("name").charClassAbbrev(abbreviation, config)
            }.build()

            val values = valuesForKey(spec, "name", 10)
            verifyValues(values, 7, 7)
        }
    }

    "test char class multiple classes" {
        val exclude = "CUSTOM"
        val spec = SpecBuilder().apply {
            val config = mapOf("exclude" to exclude)
            field("name").charClass(listOf("lower", "digits", "CUSTOM"), config)
        }.build()

        val values = valuesForKey(spec, "name", 10)
        val value = values.first()
        value.forEach { char ->
            char.shouldBeInstanceOf<Char>()
            char shouldBeIn (('a'..'z') + ('0'..'9'))
        }
    }

    "test verify class mappings" {
        CLASS_MAPPING.forEach { (key, values) ->
            val spec = SpecBuilder().apply {
                val config = mapOf("count" to 5)
                field("name").charClassAbbrev("cc-$key", config)
            }.build()

            val valuesList = valuesForKey(spec, "name", 10)
            val singleItem = valuesList.first()
            singleItem.forEach { char ->
                values.shouldContain(char.toString())
            }
        }
    }

    "test char class special escape" {
        val escape = "&?!"
        val spec = SpecBuilder().apply {
            val config = mapOf("escape" to escape, "escapeStr" to "ESCAPED:")
            field("name").charClass("special", config)
        }.build()

        val records = valuesForKey(spec, "name", 3)
        records.forEach { value ->
            escape.forEach { char ->
                if (char in value) {
                    value shouldContain "ESCAPED:$char"
                }
            }
        }
    }
})

private fun valuesForKey(spec: DataSpec, key: String, iterations: Long) =
    spec.entries(iterations).mapNotNull { it[key] as? String }

private fun verifyValues(values: List<String>, minSize: Int, maxSize: Int, exclude: String = "") {
    for (value in values) {
        value.length shouldBeInRange  minSize..maxSize
        exclude.forEach { char ->
            value shouldNotContain char.toString()
        }
    }
}

