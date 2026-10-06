package fe.linksheet.util.intent

import android.content.ComponentName
import android.content.Intent
import android.net.Uri

public fun buildIntent(
    action: String,
    data: Uri? = null,
    componentName: ComponentName? = null,
    block: Intent.() -> Unit = {},
): Intent {
    return Intent(action, data).apply {
        componentName?.let { component = it }
        block()
    }
}
