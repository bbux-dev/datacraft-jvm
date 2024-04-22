package org.datacraft

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldNot


class LoadersTest : StringSpec({
    "When init then discovered date type should be discovered" {
        val json = """
            {"foo": {"type": "date"}}
        """
        assert(TestUtils.validateJson(json))

        val spec: DataSpec = DataSpec.parseString(json)
        val loader = Loaders.init(spec)
        val supplier = loader.get("foo")
        supplier shouldNot beNull()
    }
    "When init then discovered combine type should be discovered" {
        val json = """
            {
              "foo": {"type": "combine", "refs": ["one", "two"]},
              "refs": {
                "one": ["alpha", "beta"],
                "two": [1, 2]
              }
            }
        """
        assert(TestUtils.validateJson(json))

        val spec: DataSpec = DataSpec.parseString(json)
        val loader = Loaders.init(spec)
        val supplier = loader.get("foo")
        supplier shouldNot beNull()
    }
})