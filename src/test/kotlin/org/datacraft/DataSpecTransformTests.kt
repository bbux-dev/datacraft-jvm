package org.datacraft

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json

class DataSpecTransformTests : StringSpec({

    val testCases = listOf(
        Pair(
            """{"name": [1, 2, 3, 4, 5]}""",
            """{"name": {"type": "values", "data": [1, 2, 3, 4, 5]}}"""
        ),
        Pair(
            """{"foo?prefix=TEST": [1, 2, 3, 4, 5]}""",
            """{"foo": {"type": "values", "data": [1, 2, 3, 4, 5], "config": {"prefix": "TEST"}}}"""
        ),
        Pair(
            """{"foo:uuid": {}}""",
            """{"foo": {"type": "uuid"}}"""
        ),
        Pair(
            """{"bar?suffix=END": {"type": "values", "data": [1, 2, 3, 4], "config": {"prefix": "START"}}}""",
            """{"bar": {"type": "values", "data": [1, 2, 3, 4], "config": {"prefix": "START", "suffix": "END"}}}"""
        ),
        Pair(
            """{"field_groups": [["one", "two"], ["one", "two", "three"]]}""",
            """{"field_groups": [["one", "two"], ["one", "two", "three"]]}"""
        ),
        Pair(
            """{"network:ip?cidr=2.3.4.0/16": {}}""",
            """{"network": {"type": "ip", "config": {"cidr": "2.3.4.0/16"}}}"""
        ),
        Pair(
            """{"geo:geo.pair": {}}""",
            """{"geo": {"type": "geo.pair"}}"""
        ),
        Pair(
            """{"zero_to_ten?prefix=A-": {"type": "range", "data": [0, 10, 0.5], "config": {"prefix": "C-"}}, "ztt:range?prefix=B-": [0, 10, 0.5]}""",
            """{"zero_to_ten": {"type": "range", "data": [0, 10, 0.5], "config": {"prefix": "A-"}}, "ztt": {"type": "range", "data": [0, 10, 0.5], "config": {"prefix": "B-"}}}"""
        )
    )

    testCases.forEachIndexed { index, (input, expected) ->
        "transform spec case ${index + 1}" {
            val inputJson = Json.parseToJsonElement(input)
            val expectedJson = Json.parseToJsonElement(expected)

            val preProcessed = Preprocessor.preprocessSpec(inputJson)
            val result = Json.parseToJsonElement(preProcessed)

            result shouldBe expectedJson
        }
    }

    val invalidSpecs = listOf(
        "[]",
        "foo"
    )

    invalidSpecs.forEach { specJson ->
        "Invalid spec '$specJson' should throw SpecException" {
            shouldThrowExactly<SpecException> {
                Preprocessor.preprocessSpec(Json.parseToJsonElement(specJson))
            }
        }
    }

})


