package app.linksheet.feature.devicecompat.oneui

import android.content.Intent

public object OneUiCompat {
    public fun isOneUi(): Boolean = false
    public fun getAppOpenByDefaultIntent(packageName: String): Intent? = null
}
