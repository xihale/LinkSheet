package fe.std.uri

import fe.std.result.Result

/**
 * Advanced Uri shim to satisfy integration and util modules.
 */
public interface StdUrl {
    public val scheme: String?
    public val host: String?
    public val port: Int?
    public val userInfo: String?
    public val pathSegments: List<String>
    public val fragment: String?
    public val queryParams: Map<String, List<String>>
    
    override fun toString(): String
}

public object StdUrlFactory {
    public fun fromString(url: String): Result<StdUrl> {
        return Result.success(object : StdUrl {
            override val scheme: String? = null
            override val host: String? = null
            override val port: Int? = null
            override val userInfo: String? = null
            override val pathSegments: List<String> = emptyList()
            override val fragment: String? = null
            override val queryParams: Map<String, List<String>> = emptyMap()
            override fun toString(): String = url
        })
    }
}

public fun String.toStdUrlOrNull(): StdUrl? = StdUrlFactory.fromString(this).getOrNull()
public fun String.toStdUrlOrThrow(): StdUrl = toStdUrlOrNull() ?: throw IllegalArgumentException("Invalid URL")

public fun buildUrl(policy: Any? = null, block: StdUrlBuilder.() -> Unit): StdUrl {
    return object : StdUrl {
        override val scheme: String? = null
        override val host: String? = null
        override val port: Int? = null
        override val userInfo: String? = null
        override val pathSegments: List<String> = emptyList()
        override val fragment: String? = null
        override val queryParams: Map<String, List<String>> = emptyMap()
        override fun toString(): String = ""
    }
}

public interface StdUrlBuilder {
    public var scheme: String?
    public var host: String?
    public var port: Int?
    public var userInfo: String?
    public var pathSegments: List<String>
    public var fragment: String?
    public fun setParameters(params: Map<String, List<String>>)
}

public object EncodingPolicy {
    public val Default: Any = Any()
}

/**
 * Host utility shims.
 */
public class CompatUriHost(public val value: String)

public val StdUrl.compatHost: CompatUriHost get() = CompatUriHost(host ?: "")

/**
 * Minimal Uri class for basic usage.
 */
public class Uri(public val value: String) {
    override fun toString(): String = value
}

public fun String.toUri(): Uri = Uri(this)
