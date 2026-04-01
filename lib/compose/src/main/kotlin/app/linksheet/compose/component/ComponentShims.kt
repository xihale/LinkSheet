package app.linksheet.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar

/**
 * Legacy UI Component shims.
 */

@Composable
public fun AppIconImage(
    packageName: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    // Stub
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SearchTopAppBar(
    title: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) }
    )
}
