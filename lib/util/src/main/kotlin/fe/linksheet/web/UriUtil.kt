package fe.linksheet.web

import fe.std.uri.StdUrl

public object UriUtil {
    public fun getPath(url: String): String = ""
}

public fun StdUrl.isHttp(): Boolean = scheme == "http" || scheme == "https"
