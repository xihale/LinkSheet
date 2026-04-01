package fe.linksheet.web

/**
 * Shims for UriUtil.kt
 */

public inline fun <T, R> Iterable<T>.mapToSet(transform: (T) -> R): Set<R> {
    return map(transform).toSet()
}

public object URLStringUtils {
    public fun decode(url: String): String = url
}
