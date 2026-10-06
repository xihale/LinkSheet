package fe.linksheet.module.workmanager

import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkManager
import fe.android.lifecycle.LifecycleAwareService
import fe.android.lifecycle.koin.extension.service
import fe.linksheet.module.preference.PreferenceRepositoryModule
import org.koin.dsl.module

val WorkDelegatorServiceModule = module {
    includes(PreferenceRepositoryModule)
    service<WorkDelegatorService> {
        WorkDelegatorService(
            workManager = WorkManager.getInstance(applicationContext),
        )
    }
}

class WorkDelegatorService(
    val workManager: WorkManager,
) : LifecycleAwareService {

    override suspend fun onAppInitialized(owner: LifecycleOwner) {
    }
}

