package fe.linksheet.web

import fe.std.uri.StdUrl
import fe.std.uri.CompatUriHost

/**
 * Shims for HostUtil.kt
 */

public val StdUrl.compatHost: CompatUriHost get() = CompatUriHost(this.host ?: "")

public object HostName {
    public fun from(host: String): Any = Any()
}

public class ShimAddress(public val value: String) {
    public val isAddress: Boolean = false
}

public fun String.asAddress(): ShimAddress = ShimAddress(this)

public val Any.isAddress: Boolean get() = false
public val Any.value: String get() = this.toString()
