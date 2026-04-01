package app.linksheet.api.preference

import kotlinx.coroutines.flow.Flow

/**
 * Standard interface for preference repositories.
 * Replaces legacy ComposeKit's FlowPreferenceRepository.
 */
interface AppPreferenceRepository {
    fun <T> get(key: String, defaultValue: T): Flow<T>
    suspend fun <T> set(key: String, value: T)
}
