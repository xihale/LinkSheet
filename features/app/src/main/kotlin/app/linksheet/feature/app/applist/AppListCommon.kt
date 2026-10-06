package app.linksheet.feature.app.applist

import app.linksheet.feature.app.core.IAppInfoBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

public fun <T : IAppInfoBase> List<T>.filterBy(query: String): List<T> {
    if (query.isEmpty()) return this
    return filter { it.label.contains(query, ignoreCase = true) || it.packageName.contains(query, ignoreCase = true) }
}

public class AppListCommon<T : IAppInfoBase>(
    apps: Flow<List<T>?>,
    scope: CoroutineScope,
) {
    public val searchQuery = MutableStateFlow("")
    public val sortState = MutableStateFlow(SortByState(SortType.AZ, true))
    public val filterState = MutableStateFlow(FilterState(StateModeFilter.ShowAll, TypeFilter.All, true))

    public val appsFiltered: StateFlow<List<T>?> = combine(apps, searchQuery) { list, query ->
        list?.filterBy(query)
    }.stateIn(scope, SharingStarted.WhileSubscribed(5_000), null)
}

