package org.datacraft

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe


class FieldGroupTests : StringSpec({

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

    "should throw spec exception when not list of lists" {
        val json = """
        {
          "foo": { "type": "uuid" },
          "bar": { "type": "uuid" },
          "baz": { "type": "uuid" },
          "field_groups": ["foo", "bar"]
        }
        """
        shouldThrow<SpecException> {
            Datacraft.entries(json, 4)
        }
    }

    "should throw spec exception when not list of lists of strings" {
        val json = """
        {
          "foo": { "type": "uuid" },
          "bar": { "type": "uuid" },
          "baz": { "type": "uuid" },
          "field_groups": [
            ["foo", "bar"],
            [1, 3, 5]
          ]
        }
        """
        shouldThrow<SpecException> {
            Datacraft.entries(json, 4)
        }
    }

    "should throw spec exception when not all elements resolvable in list of lists" {
        val json = """
        {
          "foo": { "type": "uuid" },
          "bar": { "type": "uuid" },
          "baz": { "type": "uuid" },
          "field_groups": [
            ["foo", "bar"],
            ["baz", "barf"]
          ]
        }
        """
        shouldThrow<SpecException> {
            Datacraft.entries(json, 4)
        }
    }

    "should be valid with weighted field groups" {
        val json = """
        {
          "foo": { "type": "uuid" },
          "bar": { "type": "uuid" },
          "baz": { "type": "uuid" },
          "field_groups": {
            "0.500001": ["foo", "bar"],
            "0.500002": ["bar", "baz"]
          }
        }
        """

        val entries = Datacraft.entries(json, 100)

        val uniqueKeys = entries.flatMap { it.keys }.toSet()

        uniqueKeys.size shouldBe 3
    }

    "should throw spec exception when not all elements resolvable in weighted lists" {
        val json = """
        {
          "foo": { "type": "uuid" },
          "bar": { "type": "uuid" },
          "baz": { "type": "uuid" },
          "field_groups": {
            "0.500001": ["foo", "bar"],
            "0.500002": ["barf", "baz"]
          }
        }
        """
        shouldThrow<SpecException> {
            Datacraft.entries(json, 4)
        }
    }

    "should throw spec exception with values not list in weighted field groups" {
        val json = """
        {
          "foo": { "type": "uuid" },
          "bar": { "type": "uuid" },
          "baz": { "type": "uuid" },
          "field_groups": {
            "0.5": ["foo", "bar"],
            "0.5": "baz"
          }
        }
        """
        shouldThrow<SpecException> {
            Datacraft.entries(json, 4)
        }
    }
})