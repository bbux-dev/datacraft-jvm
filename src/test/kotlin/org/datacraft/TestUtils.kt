package org.datacraft


import com.google.gson.Gson
import java.util.Map

object TestUtils {
    fun validateJson(json: String?): Boolean {
        val gson: Gson = Gson()
        try {
            gson.fromJson(json, Map::class.java)
        } catch (e: Exception) {
            return false
        }
        return true
    }
}
