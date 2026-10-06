package app.linksheet.feature.app.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import fe.android.compose.icon.IconPainter

object EmptyIconPainter : IconPainter {
    @Composable
    override fun rememberPainter(): Painter {
        return EmptyPainter
    }
}

private object EmptyPainter : Painter() {
    override val intrinsicSize = Size.Zero
    override fun DrawScope.onDraw() {}
}
