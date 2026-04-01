package android.content.pm


fun PackageManager.getInstallerFor(packageName: String): String? {
    return when {
        AndroidVersion.isAtLeastApi30R() -> getInstallSourceInfo(packageName).initiatingPackageName
        else -> getInstallerPackageName(packageName)
    }
}
