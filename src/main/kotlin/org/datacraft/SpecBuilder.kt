package org.datacraft


/**
 * SpecBuilder is a builder class for creating DataSpec objects.
 *
 * This class provides a fluent API for defining fields and their specifications
 * for data generation.
 */
class SpecBuilder {

    private val fields: MutableMap<String, FieldSpec> = mutableMapOf()
    private val refs: MutableMap<String, FieldSpec> = mutableMapOf()

    /**
     * Defines a field with a specification.
     *
     * @param fieldName The name of the field.
     * @return The FieldBuilder instance for chaining.
     */
    fun field(fieldName: String): FieldBuilder {
        return FieldBuilder(this, fieldName)
    }

    /**
     * Defines a field with a specification all in the key.
     *
     * Example:
     * `id:uuid`
     *
     * @param fieldPlusType The name of the field.
     * @param data optional data field if needed for spec
     * @param config optional configuration
     * @return The FieldBuilder instance for chaining.
     */
    @JvmOverloads
    fun fieldByKey(fieldPlusType: String, data: Any? = null, config: Map<String, Any> = emptyMap()): SpecBuilder {
        return byKey(fieldPlusType, config, data, false)
    }

    /**
     * Defines a ref with a specification all in the key.
     *
     * Example:
     * `ts:date.iso`
     *
     * @param fieldPlusType The name of the field.
     * @param data optional data field if needed for spec
     * @param config optional configuration
     * @return The FieldBuilder instance for chaining.
     */
    @JvmOverloads
    fun refByKey(fieldPlusType: String, data: Any? = null, config: Map<String, Any> = emptyMap()): SpecBuilder {
        return byKey(fieldPlusType, config, data, true)
    }

    private fun byKey(
        fieldPlusType: String,
        config: Map<String, Any>,
        data: Any?,
        isRef: Boolean
    ): SpecBuilder {
        if (!fieldPlusType.contains(":")) {
            throw SpecException("fieldPlusKey requires : formatted field:type format")
        }
        val parts = Preprocessor.parseKey(fieldPlusType)
        val mergedConfig = config.toMutableMap()
        mergedConfig.putAll(parts.third)
        val fieldSpec = mutableMapOf(
            "type" to parts.second!!,
            "config" to mergedConfig
        )
        if (data != null) {
            fieldSpec["data"] = data
        }
        addField(parts.first, fieldSpec.toMap(), isRef)
        return this
    }


    /**
     * Defines a ref field with a range specification.
     *
     * @param fieldName The name of the reference.
     * @return The FieldBuilder instance for chaining.
     */
    fun ref(fieldName: String): FieldBuilder {
        return FieldBuilder(this, fieldName, true)
    }

    /**
     * Builds the DataSpec object.
     *
     * @return The DataSpec object.
     */
    fun build(): DataSpec {
        return DataSpec(fields, refs)
    }

    internal fun addField(fieldName: String, fieldSpec: Map<String, Any>, isRef: Boolean) {
        val spec =
            DataSpec.fieldSpecFrom(fieldSpec) ?: throw SpecException("Unable to create field/ref spec from $fieldSpec")
        if (isRef) {
            refs[fieldName] = spec
        } else {
            fields[fieldName] = spec
        }
    }

    /**
     * FieldBuilder is a helper class for building individual field specifications.
     */
    class FieldBuilder(
        private val parent: SpecBuilder,
        private val fieldName: String,
        private val isRef: Boolean = false
    ) {

        /**
         * Specifies a range for the field.
         *
         * @param start The start of the range.
         * @param end The end of the range.
         * @param config Optional configuration for the range.
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun range(start: Int, end: Int, config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "range",
                "data" to listOf(start, end),
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Specifies a set of values for the field.
         *
         * @param values The list of values.
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun values(values: List<Any>, config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "values",
                "data" to values,
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Specifies a set of values to weights for the field.
         *
         * @param weights The map of value to weight
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun values(weights: Map<String, Number>, config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "values",
                "data" to weights,
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Builds a uuid spec
         *
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun uuid(config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "uuid",
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Builds a combine spec
         *
         * @param refs to combine
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun combine(refs: List<String>, config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "combine",
                "refs" to refs,
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Builds a date spec
         *
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun date(config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "date",
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Builds a date.iso spec
         *
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun dateIso(config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "date.iso",
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Builds a date.iso.millis spec
         *
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun dateIsoMs(config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "date.iso.millis",
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Builds a date.epoch spec
         *
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun dateEpoch(config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "date.epoch",
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Builds a date.epoch.millis spec
         *
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        @JvmOverloads
        fun dateEpochMs(config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "date.epoch.millis",
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Builds a ref spec
         *
         * @param ref for ref
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        fun ref(ref: Any, config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "ref",
                "ref" to ref,
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        /**
         * Builds a char_class spec
         *
         * @param data for char_class
         * @param config optional configuration
         * @return The SpecBuilder instance for chaining.
         */
        fun charClass(data: Any, config: Map<String, Any> = emptyMap()): SpecBuilder {
            val fieldSpec = mapOf(
                "type" to "char_class",
                "data" to data,
                "config" to config
            )
            parent.addField(fieldName, fieldSpec, isRef)
            return parent
        }

        fun charClassAbbrev(ccAbbrev: String, config: Map<String, Any?> = emptyMap()): SpecBuilder {
            val fieldSpec = mutableMapOf<String, Any>("type" to "char_class_abbrev")

            if (ccAbbrev.startsWith("cc-")) {
                fieldSpec["type"] = ccAbbrev
            } else {
                fieldSpec["type"] = "cc-$ccAbbrev"
            }

            if (config.isNotEmpty()) {
                fieldSpec["config"] = config
            }

            parent.addField(fieldName, fieldSpec.toMap(), isRef)
            return parent
        }


    }
}

