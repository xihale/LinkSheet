package app.linksheet.feature.app.core

import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import fe.android.compose.icon.IconPainter
import fe.linksheet.util.extension.android.componentName

public typealias IAppInfo = AppInfo

public data class AppInfo(
    public override val packageName: String,
    public override val label: String,
    public override val icon: IconPainter = EmptyIconPainter,
    public val flags: Int = 0,
) : IAppInfoBase {
    override val componentName: ComponentName get() = ComponentName(packageName, "")
}

public interface IAppInfoBase {
    public val label: String
    public val packageName: String
    public val componentName: ComponentName
    public val icon: IconPainter
}

public data class ActivityAppInfo(
    public val info: ActivityInfo,
    override val label: String,
    override val icon: IconPainter = EmptyIconPainter,
) : IAppInfoBase {
    override val packageName: String get() = info.packageName
    override val componentName: ComponentName get() = info.componentName
}

public data class ApplicationAppInfo(
    public val info: ApplicationInfo,
    override val label: String,
) : IAppInfoBase {
    override val packageName: String get() = info.packageName
    override val componentName: ComponentName get() = ComponentName(info.packageName, "")
    override val icon: IconPainter get() = EmptyIconPainter
}

public enum class LinkHandling {
    Always, Never, Ask, Allowed, Disallowed, Browser, Unsupported
}

public data class DomainVerificationAppInfo(
    val appInfo: AppInfo,
    val handling: LinkHandling,
    val stateNone: List<String> = emptyList(),
    val stateSelected: List<String> = emptyList(),
    val stateVerified: List<String> = emptyList(),
)
