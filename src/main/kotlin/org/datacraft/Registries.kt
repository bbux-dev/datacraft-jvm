package org.datacraft

object Registries {
    private val defaultValues: Map<String, Any> = mapOf(
        "date_format" to "yyyy-MM-dd",
        "date_duration_days" to 30,
        "date_stddev_days" to 5.0,
        "outfile_prefix" to "generated",
        "outfile_extension" to "",
        "data_dir" to "."
    )

    @Suppress("UNCHECKED_CAST")
    fun <T> getDefault(key: String): T =
        defaultValues[key] as? T ?: throw IllegalArgumentException("No default value for key: $key")
}
