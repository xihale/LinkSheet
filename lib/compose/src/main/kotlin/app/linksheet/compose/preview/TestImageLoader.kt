package app.linksheet.compose.preview

import android.content.Context
import coil3.ImageLoader

public fun TestImageLoader(context: Context, block: Any? = null): ImageLoader {
    return ImageLoader.Builder(context).build()
}
