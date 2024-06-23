package org.datacraft

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import java.io.File

class DatacraftCLITest: StringSpec({

    "should load JSON file correctly" {
        val jsonContent = """{
            "ts:date.iso": {},
            "names": ["bob", "joe", "bobby-joe"],
            "id:combine": {
                "refs": ["a", "b"]
            },
            "refs": {
                "a": [1, 2, 3, 4],
                "b": ["a", "b", "c", "d"]
            }
        }
        """
        val tempJsonFile = File.createTempFile("spec", ".json").apply {
            writeText(jsonContent)
        }

        val dataSpec = DatacraftCLI.loadJsonOrYaml(tempJsonFile)
        dataSpec.data["ts"].shouldNotBeNull()
        dataSpec.data["names"].shouldNotBeNull()
        dataSpec.data["id"].shouldNotBeNull()
    }

    "should load YAML file correctly" {
        val yamlContent = """
        ts:
          type: date.iso
        names:
          type: values
          data: [bob, joe, bobby-joe]
        id:
          type: combine
          refs: [a, b]
        refs:
          a:
            type: values
            data: [1, 2, 3, 4]
          b:
            type: values
            data: [a, b, c, d]
        """.trimIndent()
        val tempYamlFile = File.createTempFile("spec", ".yaml").apply {
            writeText(yamlContent)
        }

        val dataSpec = DatacraftCLI.loadJsonOrYaml(tempYamlFile)
        dataSpec.data["ts"].shouldNotBeNull()
        dataSpec.data["names"].shouldNotBeNull()
        dataSpec.data["id"].shouldNotBeNull()
    }
})