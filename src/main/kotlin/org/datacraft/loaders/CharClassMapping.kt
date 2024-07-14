package org.datacraft.loaders

object CharClassMappings {
    val CLASS_MAPPING: Map<String, String> = mapOf(
        "ascii" to (' '..'~').joinToString(""),
        "lower" to ('a'..'z').joinToString(""),
        "upper" to ('A'..'Z').joinToString(""),
        "letters" to ('a'..'z').joinToString("") + ('A'..'Z').joinToString(""),
        "word" to ('a'..'z').joinToString("") + ('A'..'Z').joinToString("") + ('0'..'9').joinToString("") + "_",
        "printable" to (' '..'~').joinToString(""),
        "visible" to (' '..'~').filterNot { it.isWhitespace() }.joinToString(""),
        "punctuation" to listOf(
            '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?',
            '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~'
        ).joinToString(""),
        "special" to listOf(
            '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?',
            '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~'
        ).joinToString(""),
        "digits" to ('0'..'9').joinToString(""),
        "hex" to ('0'..'9').joinToString("") + ('a'..'f').joinToString("") + ('A'..'F').joinToString(""),
        "hex-lower" to ('0'..'9').joinToString("") + ('a'..'f').joinToString(""),
        "hex-upper" to ('0'..'9').joinToString("") + ('A'..'F').joinToString("")
    )
}
