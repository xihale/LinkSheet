package app.linksheet.feature.devicecompat.miui

import android.content.Context
import android.content.Intent

public object MiuiCompat {
    public fun isMiui(): Boolean = false
    public fun checkOp(context: Context, op: Int): Int = 0
    public fun getAppOpenByDefaultIntent(packageName: String): Intent? = null
}
