package fe.composekit.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

public interface TextContent {
    public val value: String
}

public val String.textContent: TextContent
    get() = object : TextContent {
        override val value: String = this@textContent
    }

@Composable
public fun SearchTopAppBar(
    title: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    textContent: TextContent? = null
) {
    // Stub
}
