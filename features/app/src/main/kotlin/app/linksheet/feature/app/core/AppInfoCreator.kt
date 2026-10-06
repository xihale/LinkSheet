package app.linksheet.feature.app.core

import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

public class AppInfoCreator(private val packageManager: PackageManager) {
    public fun create(info: ActivityInfo): ActivityAppInfo {
        return ActivityAppInfo(info, info.loadLabel(packageManager).toString())
    }

    public fun create(info: ApplicationInfo): ApplicationAppInfo {
        return ApplicationAppInfo(info, info.loadLabel(packageManager).toString())
    }

    public fun toAppInfo(applicationInfo: ApplicationInfo, installTime: Long?): AppInfo {
        return AppInfo(
            packageName = applicationInfo.packageName,
            label = applicationInfo.loadLabel(packageManager).toString(),
            flags = applicationInfo.flags,
        )
    }

    public fun toActivityAppInfo(resolveInfo: android.content.pm.ResolveInfo): ActivityAppInfo {
        val activityInfo = resolveInfo.activityInfo
        return ActivityAppInfo(
            info = activityInfo,
            label = resolveInfo.loadLabel(packageManager).toString(),
        )
    }
}
