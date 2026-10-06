package android.content.pm

import fe.std.process.android.AndroidVersion


fun PackageManager.getInstallerFor(packageName: String): String? {
    return when {
        AndroidVersion.isAtLeastApi30R() -> getInstallSourceInfo(packageName).initiatingPackageName
        else -> getInstallerPackageName(packageName)
    }
}
