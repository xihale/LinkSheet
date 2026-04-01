package app.linksheet.feature.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fe.composekit.compose.component.SearchTopAppBar
import fe.composekit.compose.component.textContent

@Composable
public fun AppFilterSearchTopAppBar(
    title: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchQuery: String = ""
) {
    SearchTopAppBar(
        title = title,
        onSearch = onSearch,
        modifier = modifier,
        textContent = searchQuery.textContent
    )
}
