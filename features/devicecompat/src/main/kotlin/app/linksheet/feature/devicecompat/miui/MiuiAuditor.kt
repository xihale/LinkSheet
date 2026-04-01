package app.linksheet.feature.devicecompat.miui

import android.content.Context

public object MiuiAuditor {
    public fun isRestricted(context: Context, packageName: String): Boolean = false
}
