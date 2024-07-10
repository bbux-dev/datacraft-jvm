package org.datacraft

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class FieldSpecTest : StringSpec({

    "forType with ref should populate ref" {
        val spec = FieldSpec.forType("ref", mapOf("type" to "ref", "ref" to "foo"))
        val refSpec = spec as? RefFieldSpec
            ?: throw IllegalArgumentException("Spec must be a RefFieldSpec")
        refSpec.ref.shouldNotBeNull()
    }
})
