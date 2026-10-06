package fe.linksheet.module.versiontracker

import androidx.lifecycle.LifecycleOwner
import fe.android.lifecycle.LifecycleAwareService
import fe.android.lifecycle.koin.extension.service
import fe.linksheet.BuildConfig
import fe.linksheet.feature.systeminfo.SystemInfoService
import fe.linksheet.module.preference.PreferenceRepositoryModule
import fe.linksheet.module.preference.app.AppPreferenceRepository
import fe.linksheet.module.preference.app.AppPreferences
import fe.linksheet.module.systeminfo.SystemInfoServiceModule
import fe.linksheet.util.buildconfig.Build
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val VersionTrackerModule = module {
    includes(SystemInfoServiceModule, PreferenceRepositoryModule)

    service<VersionTracker, AppPreferenceRepository> { _, preferences ->
        VersionTracker(
            preferenceRepository = preferences,
            systemInfoService = scope.get<SystemInfoService>(),
        )
    }
}

internal class VersionTracker(
    val preferenceRepository: AppPreferenceRepository,
    private val systemInfoService: SystemInfoService,
) : LifecycleAwareService {
    private val lastVersionsService by lazy {
        LastVersionService(systemInfoService.buildInfo)
    }

    override suspend fun onAppInitialized(owner: LifecycleOwner) {
        val lastVersion = preferenceRepository.get(AppPreferences.lastVersion)

        if (Build.IsDebug) {
            // TODO: Remove once user is given the choice to opt in/out
        }

        val lastVersions = preferenceRepository.get(AppPreferences.lastVersions)
        val lastVersionJson = lastVersionsService.handleVersions(lastVersions, true)

        preferenceRepository.edit {
            lastVersionJson?.let {
                put(AppPreferences.lastVersions, it)
            }

            put(AppPreferences.lastVersion, BuildConfig.VERSION_CODE)
        }
    }

    override suspend fun onStop() {
    }
}
