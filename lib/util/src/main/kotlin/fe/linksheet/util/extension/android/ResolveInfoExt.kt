package fe.linksheet.util.extension.android

import android.content.ComponentName
import android.content.pm.ResolveInfo
import android.content.pm.ActivityInfo

/**
 * Android extension shims.
 */

public val ResolveInfo.componentName: ComponentName
    get() = ComponentName(this.activityInfo.packageName, this.activityInfo.name)

public val ActivityInfo.componentName: ComponentName
    get() = ComponentName(this.packageName, this.name)

public val ResolveInfo.packageName: String
    get() = this.activityInfo.packageName
