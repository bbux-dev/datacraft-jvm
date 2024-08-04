package org.datacraft.suppliers

import org.datacraft.SpecException
import org.datacraft.ValueSupplier
import org.datacraft.models.KeyProvider

class KeySuppliers {
    companion object {
        fun fromFieldGroup(keys: List<String>, fieldGroups: Any?): KeyProvider {
            if (fieldGroups == null) {
                return KeyListProvider(keys)
            }
            when (fieldGroups) {
                is Map<*, *> -> {
                    try {
                        // Check if all the keys are numeric
                        val map = fieldGroups
                            .mapKeys { it.key.toString().toFloat() }
                            .mapValues { entry ->
                                when (val value = entry.value) {
                                    is List<*> -> value.map { it.toString() }
                                    else -> throw SpecException("Unsupported field group value type ${value?.let { it::class.simpleName } ?: "null"}")
                                }
                            }
                        return createWeightedKeyProvider(keys, map)
                    } catch (e: NumberFormatException) {
                        return parseNamedFieldGroups(keys, fieldGroups)
                    }
                }

                is List<*> -> return parseFieldGroupsAsLists(keys, fieldGroups)
                else -> throw IllegalArgumentException("Invalid type for field_groups")
            }
        }

        private fun parseNamedFieldGroups(keys: List<String>, fieldGroups: Map<*, *>): KeyProvider {
            TODO("Not yet implemented")
        }

        private fun createWeightedKeyProvider(keys: List<String>, map: Map<Float, List<String>>): KeyProvider {
            TODO("Not yet implemented")
        }

        @Suppress("UNCHECKED_CAST")
        private fun parseFieldGroupsAsLists(keys: List<String>, fieldGroups: List<*>): KeyProvider {
            // are they all lists?
            if (!fieldGroups.all { it is List<*> }) {
                throw SpecException("Field groups as list must contain only List of Lists of Strings")
            }
            // are they all lists of lists of strings?
            if (!fieldGroups.all { nested ->
                    (nested as List<*>).all { it is String }
                }) {
                throw SpecException("Field groups as list must contain only List of Lists of Strings")
            }
            // now lets make sure all the fields are resolvable
            val flattenedFieldGroups = fieldGroups.flatMap { it as List<String> }

            // Ensure all elements in flattenedFieldGroups exist in keys
            val notInKeys = flattenedFieldGroups.filterNot { it in keys }
            if (notInKeys.isNotEmpty()) {
                throw SpecException("All elements in fieldGroups must exist in Data Spec. Undefined keys: $notInKeys")
            }
            val fieldGroupLists = fieldGroups.map { it as List<String> }
            return RotatingKeyListProvider(fieldGroupLists)
        }

    }
}

internal class KeyListProvider(private val keys: List<String>) : KeyProvider {
    override fun get(): Pair<String, List<String>> {
        return "ALL" to keys
    }
}

internal class MappedKeyListProvider(data: Map<String, List<String>>) : KeyProvider {
    private var cnt = 0
    private val keys: List<Pair<String, List<String>>> = data.map { Pair(it.key, it.value) }

    override fun get(): Pair<String, List<String>> {
        val idx = cnt % keys.size
        cnt++
        return keys[idx]
    }
}

internal class RotatingKeyListProvider(private val keys: List<List<String>>) : KeyProvider {
    private var cnt = 0

    override fun get(): Pair<String, List<String>> {
        val idx = cnt % keys.size
        cnt++
        return Pair(idx.toString(), keys[idx])
    }
}

internal class WeightedGroupKeyProvider(
    private val fieldGroups: Map<String, List<String>>,
    private val supplier: ValueSupplier<String>
) : KeyProvider {

    override fun get(): Pair<String, List<String>> {
        val key = supplier.next(0)
        val fieldGroup = fieldGroups[key] ?: throw SpecException("No key $key in field groups")
        return key to fieldGroup
    }
}

