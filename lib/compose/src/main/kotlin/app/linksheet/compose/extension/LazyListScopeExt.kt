package app.linksheet.compose.extension

import androidx.compose.foundation.lazy.LazyListScope

/**
 * Standard LazyList extensions.
 */

public fun LazyListScope.group(
    key: Any? = null,
    contentType: Any? = null,
    content: LazyListScope.() -> Unit
) {
    content()
}
