package org.datacraft

object Datacraft {
    /**
     * Generates an iterator over maps representing data records, each conforming to the specified field specifications.
     *
     * @param json the JSON version of the DataSpec
     * @param iterations the number of records to generate.
     * @return an iterator that produces a series of maps, where each map represents a data record.
     */
    fun generator(json: String, iterations: Long): Iterator<Map<String, Any?>> = object : Iterator<Map<String, Any?>> {
        val gen = DataSpec.parseString(json).generator(iterations)

        override fun hasNext(): Boolean {
            return gen.hasNext()
        }

        override fun next(): Map<String, Any?> {
            return gen.next()
        }
    }

    /**
     * Generates an iterator over maps representing data records, each conforming to the specified field specifications.
     *
     * @param rawMap the Map version of the DataSpec
     * @param iterations the number of records to generate.
     * @return an iterator that produces a series of maps, where each map represents a data record.
     */
    fun generator(rawMap: Map<String, Any?>, iterations: Long): Iterator<Map<String, Any?>> =
        object : Iterator<Map<String, Any?>> {
            val gen = DataSpec.parse(rawMap).generator(iterations)

            override fun hasNext(): Boolean {
                return gen.hasNext()
            }

            override fun next(): Map<String, Any?> {
                return gen.next()
            }
        }

    /**
     * Generates an iterator over instances of a specified type, populated with data according to the field specifications.
     *
     * This method uses Gson to serialize each map into an instance of the specified class type. This method is useful
     * when you need to produce data records as instances of a data class or other class types.
     *
     * @param iterations the number of records to generate.
     * @param type the class of the type to which the generated records should be converted.
     * @return an iterator over instances of the specified type.
     * @throws JsonSyntaxException if JSON serialization or deserialization fails.
     */
    fun <T> generateRecords(json: String, iterations: Long, type: Class<T>): Iterator<T> = object : Iterator<T> {
        val gen = DataSpec.parseString(json).generateRecords(iterations, type)

        override fun hasNext(): Boolean {
            return gen.hasNext()
        }

        override fun next(): T {
            return gen.next()
        }
    }

    /**
     * Generates an iterator over instances of a specified type, populated with data according to the field specifications.
     *
     * This method uses Gson to serialize each map into an instance of the specified class type. This method is useful
     * when you need to produce data records as instances of a data class or other class types.
     *
     * @param rawMap the Map version of the DataSpec
     * @param iterations the number of records to generate.
     * @param type the class of the type to which the generated records should be converted.
     * @return an iterator over instances of the specified type.
     */
    fun <T> generateRecords(rawMap: Map<String, Any?>, iterations: Long, type: Class<T>): Iterator<T> =
        object : Iterator<T> {
            val gen = DataSpec.parse(rawMap).generateRecords(iterations, type)

            override fun hasNext(): Boolean {
                return gen.hasNext()
            }

            override fun next(): T {
                return gen.next()
            }
        }

    /**
     * Generates a List of maps representing data records, each conforming to the specified field specifications.
     *
     * @param json the JSON version of the DataSpec
     * @param iterations the number of records to generate.
     * @return a list of generated records
     */
    fun entries(json: String, iterations: Long): List<Any> {
        return DataSpec.parseString(json).entries(iterations)
    }

    /**
     * Generates a List of maps representing data records, each conforming to the specified field specifications.
     *
     * @param rawMap the Map version of the DataSpec
     * @param iterations the number of records to generate.
     * @return a list of generated records
     */
    fun entries(rawMap: Map<String, Any?>, iterations: Long): List<Map<String, Any?>> {
        return DataSpec.parse(rawMap).entries(iterations)
    }
}
