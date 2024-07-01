package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.Loaders

internal fun testLoader(): Loader {
    val spec: DataSpec = DataSpec.parseString("{}")
    return Loaders.init(spec)
}

internal fun basicFieldSpec(
    type: String, data: Any? = null, config : Map<String, Any>? = null
) = FieldSpec.BasicFieldSpec(type, data, config)
