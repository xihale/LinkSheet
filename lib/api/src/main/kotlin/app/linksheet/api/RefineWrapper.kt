package app.linksheet.api

/**
 * Shim for RefineWrapper to break free from dev.rikka ecosystem.
 */
public object RefineWrapper {
    public fun <T> Any.cast(): T? = null
}
