package org.datacraft

import com.google.gson.Gson

object TestUtils {
    fun validateJson(json: String?): Boolean {
        val gson = Gson()
        try {
            gson.fromJson(json, Map::class.java)
        } catch (e: Exception) {
            return false
        }
        return true
    }
}
