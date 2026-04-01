package fe.kotlin.extension.iterable

public inline fun <T> Iterable<T>.filterIf(condition: Boolean, predicate: (T) -> Boolean): List<T> {
    return if (condition) filter(predicate) else toList()
}

public inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T {
    if (condition) block()
    return this
}

/**
 * Common collection extensions for feature modules.
 */
public fun <T> Iterable<T>.toPackageKeyedMap(keySelector: (T) -> String): Map<String, T> {
    return associateBy(keySelector)
}
