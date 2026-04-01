package app.linksheet.compose

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Legacy UI shims to satisfy :app module references during refactoring.
 */

public fun LazyListScope.group(
    key: Any? = null,
    contentType: Any? = null,
    content: LazyListScope.() -> Unit
) {
    content()
}

@Composable
public fun SaneScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    // Basic wrapper
    content()
}

public interface SaneLazyListScope : LazyListScope
