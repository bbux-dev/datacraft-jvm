package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

private val cidrPattern = Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(3[0-2]|[12]?[0-9])$")

class IpLoader : ValueSupplierLoader<String> {
    override fun typeNames(): List<String> = listOf("ip", "ipv4")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<String> {
        val config = spec.config ?: mapOf()
        val cidr: String? = config["cidr"] as? String
        if (cidr != null && !cidrPattern.matches(cidr)) {
            throw SpecException("Invalid CIDR format: $cidr")
        }
        return Suppliers.ipV4Address(cidr = cidr)
    }
}