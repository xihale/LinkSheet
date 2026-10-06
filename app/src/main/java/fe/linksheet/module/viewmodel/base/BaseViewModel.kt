package fe.linksheet.module.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fe.android.preference.helper.Preference
import fe.composekit.preference.FlowPreferenceRepository
import fe.composekit.preference.ViewModelStatePreference
import fe.linksheet.module.preference.app.AppPreferenceRepository
import fe.linksheet.module.preference.app.AppPreferences
import kotlinx.coroutines.Dispatchers

abstract class BaseViewModel(
    preferenceRepository: AppPreferenceRepository,
//  protected val stateCache: StateCache = StateCache()
) : ViewModel() {
    val alwaysShowPackageName = preferenceRepository.asViewModelState(AppPreferences.alwaysShowPackageName)

    protected fun FlowPreferenceRepository.asViewModelState(
        preference: Preference.Default<Boolean>,
    ): ViewModelStatePreference<Boolean, Boolean, Preference.Default<Boolean>> =
        ViewModelStatePreference(
            preference = preference,
            get = { getBoolean(it) },
            put = { pref, value -> put(pref, value) },
            coroutineScope = viewModelScope,
            context = Dispatchers.Main.immediate,
        )

    protected fun FlowPreferenceRepository.asViewModelState(
        preference: Preference.Default<Int>,
    ): ViewModelStatePreference<Int, Int, Preference.Default<Int>> =
        ViewModelStatePreference(
            preference = preference,
            get = { getInt(it) },
            put = { pref, value -> put(pref, value) },
            coroutineScope = viewModelScope,
            context = Dispatchers.Main.immediate,
        )

    protected fun FlowPreferenceRepository.asViewModelState(
        preference: Preference.Default<Long>,
    ): ViewModelStatePreference<Long, Long, Preference.Default<Long>> =
        ViewModelStatePreference(
            preference = preference,
            get = { getLong(it) },
            put = { pref, value -> put(pref, value) },
            coroutineScope = viewModelScope,
            context = Dispatchers.Main.immediate,
        )

    protected fun FlowPreferenceRepository.asViewModelState(
        preference: Preference.Nullable<String>,
    ): ViewModelStatePreference<String, String, Preference.Nullable<String>> =
        ViewModelStatePreference(
            preference = preference,
            get = { getString(it) },
            put = { pref, value -> put(pref, value) },
            coroutineScope = viewModelScope,
            context = Dispatchers.Main.immediate,
        )

    protected fun <T : Any> FlowPreferenceRepository.asViewModelState(
        preference: Preference.Mapped<T, Boolean>,
    ): ViewModelStatePreference<T, T, Preference.Mapped<T, Boolean>> =
        ViewModelStatePreference(
            preference = preference,
            get = { getMappedByBoolean(it) },
            put = { pref, value -> putMappedToBoolean(pref, value) },
            coroutineScope = viewModelScope,
            context = Dispatchers.Main.immediate,
        )

    protected fun <T : Any> FlowPreferenceRepository.asViewModelState(
        preference: Preference.Mapped<T, Int>,
    ): ViewModelStatePreference<T, T, Preference.Mapped<T, Int>> =
        ViewModelStatePreference(
            preference = preference,
            get = { getMappedByInt(it) },
            put = { pref, value -> putMappedToInt(pref, value) },
            coroutineScope = viewModelScope,
            context = Dispatchers.Main.immediate,
        )

    protected fun <T : Any> FlowPreferenceRepository.asViewModelState(
        preference: Preference.Mapped<T, Long>,
    ): ViewModelStatePreference<T, T, Preference.Mapped<T, Long>> =
        ViewModelStatePreference(
            preference = preference,
            get = { getMappedByLong(it) },
            put = { pref, value -> putMappedToLong(pref, value) },
            coroutineScope = viewModelScope,
            context = Dispatchers.Main.immediate,
        )

    protected fun <T : Any> FlowPreferenceRepository.asViewModelState(
        preference: Preference.Mapped<T, String>,
    ): ViewModelStatePreference<T, T, Preference.Mapped<T, String>> =
        ViewModelStatePreference(
            preference = preference,
            get = { getMappedByString(it) },
            put = { pref, value -> putMappedToString(pref, value) },
            coroutineScope = viewModelScope,
            context = Dispatchers.Main.immediate,
        )

//    @JvmName
//    public final fun asState(
//        preference: Preference.Default<Boolean>
//    ): MutablePreferenceState<Boolean, Boolean, Preference.Default<Boolean>>
//
//    fe.android.preference.helper.compose.StatePreferenceRepository



    init {
//        Log.d("ViewModel", getTag())
    }

    // Unused for now
//    @Composable
//    fun bindStateLifetime(navController: NavController): () -> Unit {
//        val onBack: () -> Unit = {
//            stateCache.close()
//            navController.popBackStack()
//        }
//
//        BackHandler(true, onBack)
//
//        val lifecycleState = LocalLifecycleOwner.current.lifecycle.observeAsState()
//        LaunchedEffect(lifecycleState.state) {
//            if (lifecycleState.state == Lifecycle.Event.ON_PAUSE) stateCache.close()
//        }
//
//        return onBack
//    }
}
