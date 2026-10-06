package fe.linksheet.module.analytics

import fe.android.lifecycle.LifecycleCoroutineScope
import fe.android.lifecycle.LifecycleAwareService
import fe.android.lifecycle.koin.extension.service
import fe.linksheet.module.preference.app.AppPreferenceRepository
import org.koin.dsl.module

val AnalyticsServiceModule = module {
    service<BaseAnalyticsService, AppPreferenceRepository> { _, _ ->
        NoopAnalyticsService
    }
}

internal object NoopAnalyticsService : BaseAnalyticsService {
    override fun changeLevel(newLevel: TelemetryLevel?) {
    }

    override suspend fun onResume() {
    }

    override suspend fun onStop() {
    }

    override fun enqueue(event: AnalyticsEvent?): Boolean {
        return false
    }
}
