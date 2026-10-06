package fe.linksheet

import android.app.Application
import app.linksheet.api.DependencyProvider
import app.linksheet.compose.debug.DebugMenuSlotProvider
import app.linksheet.compose.debug.DebugPreferenceProvider
import app.linksheet.compose.debug.NoOpDebugMenuSlotProvider
import app.linksheet.compose.debug.NoOpDebugPreferenceProvider
import app.linksheet.feature.browser.PrivateBrowsingModule
import app.linksheet.feature.devicecompat.CompatModule
import app.linksheet.feature.devicecompat.miui.MiuiCompatProvider
import app.linksheet.feature.devicecompat.miui.RealMiuiCompatProvider
import app.linksheet.feature.devicecompat.oneui.OneUiCompatProvider
import app.linksheet.feature.devicecompat.oneui.RealOneUiCompatProvider
import app.linksheet.feature.downloader.DownloaderModule
import app.linksheet.feature.engine.LinkEngineFeatureModule
import app.linksheet.feature.libredirect.LibRedirectFeatureModule
import app.linksheet.feature.libredirect.LibRedirectMigratorModule
import app.linksheet.feature.profile.ProfileFeatureModule
import app.linksheet.feature.shizuku.ShizukuModule
import app.linksheet.feature.wiki.WikiFeatureModule
import fe.android.lifecycle.CurrentActivityObserver
import fe.android.lifecycle.ProcessServiceRegistry
import fe.android.lifecycle.koin.extension.applicationLifecycle
import fe.droidkit.koin.androidApplicationContext
import fe.gson.GlobalGsonModule
import fe.gson.context.GlobalGsonContext
import fe.linksheet.activity.CrashHandlerActivity
import fe.linksheet.feature.app.AppFeatureModule
import fe.linksheet.module.clock.ClockModule
import fe.linksheet.module.database.DatabaseModule
import fe.linksheet.module.http.HttpModule
import fe.linksheet.module.language.AppLocaleModule
import fe.linksheet.module.log.DefaultLogModule
import fe.linksheet.module.log.file.entry.LogEntry
import fe.linksheet.module.log.file.entry.LogEntryDeserializer
import fe.linksheet.module.paste.PasteServiceModule
import fe.linksheet.module.preference.PreferenceRepositoryModule
import fe.linksheet.module.preference.state.AppStateServiceModule
import fe.linksheet.module.receiver.BroadcastEventBusModule
import fe.linksheet.module.refine.RefineModule
import fe.linksheet.module.repository.module.RepositoryModule
import fe.linksheet.module.shizuku.ShizukuServiceModule
import fe.linksheet.module.systeminfo.SystemInfoServiceModule
import fe.linksheet.module.viewmodel.module.ViewModelModule
import fe.linksheet.util.serialization.HttpUrlTypeAdapter
import fe.linksheet.util.serialization.UriTypeAdapter
import fe.std.process.android.AndroidVersion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mozilla.components.support.base.log.Log
import mozilla.components.support.base.log.sink.AndroidLogSink
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.time.LocalDateTime
import kotlin.system.exitProcess

open class LinkSheetApp : Application(), DependencyProvider {
    val startupTime: LocalDateTime = LocalDateTime.now()

    private val currentActivityObserver = CurrentActivityObserver()
    private val lifecycleObserver by lazy { ProcessServiceRegistry() }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(currentActivityObserver)
        Log.addSink(AndroidLogSink())

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            CrashHandlerActivity.start(this, throwable)
            exitProcess(2)
        }

        GlobalGsonContext.configure {
            registerTypeAdapter(LogEntry::class.java, LogEntryDeserializer)
            UriTypeAdapter.register(this)
            HttpUrlTypeAdapter.register(this)
        }

        if (AndroidVersion.isAtLeastApi28P()) {
            HiddenApiBypass.addHiddenApiExemptions("")
        }

        val koinModules = provideKoinModules()
        startKoin {
            androidLogger()
            androidApplicationContext(this@LinkSheetApp)
            applicationLifecycle(lifecycleObserver)
            if (!androidx.work.WorkManager.isInitialized()) {
                workManagerFactory()
            }
            modules(koinModules)
        }

        lifecycleObserver.onAppInitialized()
    }

    override fun provideKoinModules(): List<Module> {
        return listOf(
            RefineModule,
            ClockModule,
            SystemInfoServiceModule,
            PrivateBrowsingModule,
            AppFeatureModule,
            AppLocaleModule,
            ShizukuServiceModule,
            GlobalGsonModule,
            PreferenceRepositoryModule,
            DefaultLogModule,
            provideCompatProvider(),
            CompatModule,
            LibRedirectMigratorModule,
            DatabaseModule,
            RepositoryModule,
            HttpModule,
            DownloaderModule,
            ViewModelModule,
            PasteServiceModule,
            ProfileFeatureModule,
            AppStateServiceModule,
            provideDebugModule(),
            BroadcastEventBusModule,
            ShizukuModule,
            LibRedirectFeatureModule,
            LinkEngineFeatureModule,
            WikiFeatureModule,
        )
    }

    override fun provideCompatProvider(): Module {
        return module {
            single<MiuiCompatProvider> { RealMiuiCompatProvider(get(), get()) }
            single<OneUiCompatProvider> { RealOneUiCompatProvider(get()) }
        }
    }

    override fun provideAnalyticsClient(): Module {
        return module { }
    }

    override fun provideDebugModule(): Module {
        return module {
            single<DebugMenuSlotProvider> { NoOpDebugMenuSlotProvider }
            single<DebugPreferenceProvider> { NoOpDebugPreferenceProvider }
        }
    }

    fun currentActivity(): StateFlow<android.app.Activity?> {
        return MutableStateFlow(null)
    }
}
