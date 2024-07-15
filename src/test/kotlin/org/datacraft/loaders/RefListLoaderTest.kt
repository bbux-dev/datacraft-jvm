package org.datacraft.loaders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.datacraft.*

class RefListLoaderTest : StringSpec({

    "When valid spec ref_list supplier is created" {
        // given
        val loader = RefListLoader()
        loader.typeNames() shouldBe listOf("ref_list")
        val refs = listOf("foo", "bar")
        val spec = RefListFieldSpec(null, refs)
        // when
        val supplier = loader.load(spec, testLoader("""{"foo": 42, "bar": 77}"""))
        // then
        supplier.next(1).shouldNotBeNull()
        supplier.next(2) shouldBe listOf(42.0, 77.0)
    }

    "When invalid ref_list spec SpecException in thrown" {
        // given
        val loader = RefListLoader()
        // bad data, references not defined in spec
        val refs = listOf("foo", "bar")
        val spec = RefListFieldSpec(null, refs)
        // when, then
        shouldThrow<SpecException> {
            loader.load(spec, testLoader())
        }
    }
})
