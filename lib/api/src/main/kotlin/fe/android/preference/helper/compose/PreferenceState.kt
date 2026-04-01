package fe.android.preference.helper.compose

import fe.android.preference.helper.Preference
import kotlinx.coroutines.flow.StateFlow

/**
 * Platform-independent preference state.
 * Actual Compose implementation will be moved to :core-ui or similar.
 */
public interface StatePreference<T> {
    public val key: String
    public val state: StateFlow<T>
}

public interface MutablePreferenceState<T : Any, NT, P : Preference<T, NT>> {
    public val key: String
    public val preference: P
    public val value: T
    public fun update(value: T)
}
