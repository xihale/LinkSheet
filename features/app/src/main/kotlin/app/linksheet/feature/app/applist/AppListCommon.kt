package app.linksheet.feature.app.applist

import app.linksheet.feature.app.core.ActivityAppInfo

public fun List<ActivityAppInfo>.filterBy(query: String): List<ActivityAppInfo> {
    if (query.isEmpty()) return this
    return filter { it.label.contains(query, ignoreCase = true) || it.packageName.contains(query, ignoreCase = true) }
}
