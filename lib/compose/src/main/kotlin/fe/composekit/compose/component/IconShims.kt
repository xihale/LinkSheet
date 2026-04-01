package fe.composekit.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Size

public interface IconPainter {
    @Composable
    public fun rememberPainter(): Painter
}

public class BitmapIconPainter(public val bitmap: ImageBitmap) : IconPainter {
    @Composable
    override fun rememberPainter(): Painter {
        return object : Painter() {
            override val intrinsicSize: Size = Size(bitmap.width.toFloat(), bitmap.height.toFloat())
            override fun DrawScope.onDraw() {
                // Stub
            }
        }
    }
}
