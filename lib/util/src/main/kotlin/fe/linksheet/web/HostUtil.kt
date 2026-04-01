package fe.linksheet.web

import fe.std.uri.StdUrl

public object HostUtil {
    public fun getHost(url: String): String? = null
}

public fun StdUrl.isLocal(): Boolean = false
public fun StdUrl.isLoopback(): Boolean = false
