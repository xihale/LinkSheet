package app.linksheet.compose.extension

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import app.linksheet.compose.util.ListState
import fe.composekit.layout.column.SaneLazyListScope

fun <T> SaneLazyListScope.listHelper(
    @StringRes noItems: Int,
    @StringRes notFound: Int = noItems,
    listState: ListState,
    list: List<T>?,
    listKey: (T) -> Any,
    content: @Composable LazyItemScope.(T, PaddingValues, Shape) -> Unit,
) {
    when (listState) {
        ListState.Loading -> {
            // Show nothing while loading; could add a loading indicator here
        }
        ListState.NoItems -> {
            item(key = "no_items") {
                androidx.compose.material3.Text(text = stringResource(noItems))
            }
        }
        ListState.NoResult -> {
            item(key = "no_result") {
                androidx.compose.material3.Text(text = stringResource(notFound))
            }
        }
        ListState.Items -> {
            if (list != null) {
                for (item in list) {
                    item(key = listKey(item)) {
                        content(item, PaddingValues(), androidx.compose.material3.MaterialTheme.shapes.medium)
                    }
                }
            }
        }
    }
}
