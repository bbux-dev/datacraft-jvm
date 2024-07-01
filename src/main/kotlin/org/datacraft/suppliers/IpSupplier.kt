package org.datacraft.suppliers
import org.datacraft.ValueSupplier
import kotlin.random.Random

class IpSupplier(private val cidr : String? = null) : ValueSupplier<String> {
    override fun next(iteration: Long): String {
        val (baseAddress, prefixLength) = if (cidr != null) {
            val parts = cidr.split('/')
            val base = parts[0].split('.').map { it.toInt() }
            val prefix = parts[1].toInt()
            Pair(base, prefix)
        } else {
            Pair(listOf(192, 168, 0, 0), 16)
        }

        val mask = -1 shl (32 - prefixLength)
        val baseInt = (baseAddress[0] shl 24) or (baseAddress[1] shl 16) or (baseAddress[2] shl 8) or baseAddress[3]
        val randomInt = baseInt or (Random.nextInt() and mask.inv())

        return "${(randomInt shr 24) and 0xFF}.${(randomInt shr 16) and 0xFF}.${(randomInt shr 8) and 0xFF}.${randomInt and 0xFF}"
    }

}
fun main() {
    // Generate a random IPv4 address within the default 192.168.0.0/16 range
    println(IpSupplier().next(1))

    // Generate a random IPv4 address within the specified CIDR block
    println(IpSupplier("10.0.0.0/8").next(1))
}