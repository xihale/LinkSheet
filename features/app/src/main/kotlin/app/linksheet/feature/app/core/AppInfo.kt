package app.linksheet.feature.app.core

import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import fe.linksheet.util.extension.android.componentName
import fe.linksheet.util.extension.android.packageName
import fe.composekit.compose.IconPainter
import fe.composekit.compose.BitmapIconPainter

public typealias IAppInfo = AppInfo

public interface AppInfo {
    public val label: String
    public val packageName: String
    public val componentName: ComponentName
    public val icon: IconPainter get() = BitmapIconPainter(null)
}

public data class ActivityAppInfo(
    public val info: ActivityInfo,
    override val label: String
) : AppInfo {
    override val packageName: String get() = info.packageName
    override val componentName: ComponentName get() = info.componentName
}

public data class ApplicationAppInfo(
    public val info: ApplicationInfo,
    override val label: String
) : AppInfo {
    override val packageName: String get() = info.packageName
    override val componentName: ComponentName get() = ComponentName(info.packageName, "")
}

public enum class LinkHandling {
    Always, Never, Ask
}

public data class DomainVerificationAppInfo(
    val appInfo: ActivityAppInfo,
    val handling: LinkHandling
)
