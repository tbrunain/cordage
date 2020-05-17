package jp.co.layerx.cordage.crosschainatomicswap

import net.corda.testing.core.ALICE_NAME
import net.corda.testing.core.BOB_NAME
import net.corda.testing.core.CHARLIE_NAME
import net.corda.testing.core.DUMMY_BANK_A_NAME
import net.corda.testing.core.TestIdentity
import java.util.*

val ALICE = TestIdentity(ALICE_NAME)
val BOB = TestIdentity(BOB_NAME)
val CHARLIE = TestIdentity(CHARLIE_NAME)
val DUMMY_BANK_A = TestIdentity(DUMMY_BANK_A_NAME)

fun readConfig(key: String): String {
    val properties = Properties()
    val root = java.lang.System.getProperty("user.dir")

    val path = java.nio.file.Paths.get("$root/src/test/resources/config.conf")
    if (java.nio.file.Files.isReadable(path)) {
        properties.load(java.nio.file.Files.newInputStream(path))
    }

    return properties.getProperty(key, "0x0")
}
