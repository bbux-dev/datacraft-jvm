package org.datacraft.loaders

object CharClassMappings {
    private val LOWER = ('a'..'z').joinToString("")
    private val UPPER = ('A'..'Z').joinToString("")
    private val DIGITS = ('0'..'9').joinToString("")
    private val ASCII = (' '..'~').joinToString("")
    private val PUNCTUATION = listOf(
        '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?',
        '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~'
    ).joinToString("")
    private val LOWER_A_TO_F = ('a'..'f').joinToString("")
    private val UPPER_A_TO_F = ('A'..'F').joinToString("")
    val CLASS_MAPPING: Map<String, String> = mapOf(
        "ascii" to ASCII,
        "lower" to LOWER,
        "upper" to LOWER,
        "letters" to LOWER + UPPER,
        "word" to LOWER + UPPER + DIGITS + "_",
        "printable" to ASCII,
        "visible" to ASCII.map { if (it.isWhitespace()) "" else it }.joinToString(""),
        "punctuation" to PUNCTUATION,
        "special" to PUNCTUATION,
        "digits" to DIGITS,
        "hex" to DIGITS + LOWER_A_TO_F + UPPER_A_TO_F,
        "hex-lower" to DIGITS + LOWER_A_TO_F,
        "hex-upper" to DIGITS + UPPER_A_TO_F
    )
}
