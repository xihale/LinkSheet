package fe.linksheet.feature.systeminfo

import app.linksheet.api.SystemProperties
import fe.kotlin.extension.string.substringOrNull

public object RealSystemProperties : SystemProperties {
    override fun get(key: String): String? {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("getprop", key))
            process.inputStream.bufferedReader().use { it.readLine()?.trim()?.takeIf { s -> s.isNotEmpty() } }
        } catch (e: Exception) {
            null
        }
    }

    override fun getAllProperties(): Map<String, String> {
        return emptyMap() // Minimal stub for refactoring
    }
}
