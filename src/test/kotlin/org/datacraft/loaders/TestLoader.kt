package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.Loaders

internal fun testLoader(spec : String = "{}"): Loader {
    return Loaders.init(DataSpec.parseString(spec))
}

internal fun basicFieldSpec(
    type: String, data: Any? = null, ref: Any?=null, config : Map<String, Any>? = null
) = FieldSpec.BasicFieldSpec(type, data, ref, config)
