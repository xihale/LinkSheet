package fe.composekit.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * The ultimate "fake" package to satisfy all legacy ComposeKit references.
 */

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

public interface IconPainter {
    @Composable
    public fun rememberPainter(): androidx.compose.ui.graphics.painter.Painter
}

public class BitmapIconPainter(public val bitmap: Any?) : IconPainter {
    @Composable
    override fun rememberPainter(): androidx.compose.ui.graphics.painter.Painter {
        return object : androidx.compose.ui.graphics.painter.Painter() {
            override val intrinsicSize: androidx.compose.ui.geometry.Size = androidx.compose.ui.geometry.Size.Unspecified
            override fun androidx.compose.ui.graphics.drawscope.DrawScope.onDraw() {}
        }
    }
}
