package app.linksheet.api.preference

import fe.android.preference.helper.Preference
import fe.composekit.preference.ViewModelStatePreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Standard interface for preference repositories.
 * Replaces legacy ComposeKit's FlowPreferenceRepository.
 */
interface AppPreferenceRepository {
    fun <T> get(key: String, defaultValue: T): Flow<T>
    suspend fun <T> set(key: String, value: T)

    fun <T : Any, NT, P : Preference<T, NT>> asViewModelState(
        preference: P,
        scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    ): ViewModelStatePreference<T, NT, P> {
        return ViewModelStatePreference<T, NT, P>(
            preference = preference,
            get = { pref: P -> runBlocking { get(pref.key, pref.default).first() } },
            put = { pref: P, value: NT -> runBlocking { set(pref.key, value) } },
            coroutineScope = scope,
            context = Dispatchers.IO,
        )
    }
}
