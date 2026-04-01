import androidx.lifecycle.compose.collectAsStateWithLifecycle
package fe.linksheet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import app.linksheet.compose.debug.LocalUiDebug
import app.linksheet.compose.debugBorder
import app.linksheet.compose.extension.collectOnIO
import app.linksheet.core.ui.sheet.StandardBottomSheet
import app.linksheet.feature.app.core.ActivityAppInfo
import app.linksheet.feature.browser.core.Browser
import fe.linksheet.R
import fe.linksheet.activity.bottomsheet.*
import fe.linksheet.activity.bottomsheet.content.failure.FailureSheetContentWrapper
import fe.linksheet.activity.bottomsheet.content.pending.LoadingIndicatorWrapper
import fe.linksheet.composable.ui.AppTheme
import fe.linksheet.extension.android.showToast
import fe.linksheet.module.resolver.IntentResolveResult
import fe.linksheet.module.resolver.ResolveEvent
import fe.linksheet.module.resolver.ResolverInteraction
import fe.linksheet.module.resolver.util.LaunchIntent
import fe.linksheet.module.resolver.util.LaunchRawIntent
import fe.linksheet.module.viewmodel.BottomSheetViewModel
import fe.linksheet.util.intent.StandardIntents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mozilla.components.support.base.log.logger.Logger
import mozilla.components.support.utils.toSafeIntent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class BottomSheetActivity : BaseComponentActivity(), KoinComponent {
    private val logger = Logger("BottomSheetActivity")
    private val viewModel by viewModel<BottomSheetViewModel>()

    private data class LaunchKey(val url: String, val packageName: String, val timestamp: Long)

    private companion object {
        private const val LOOP_DETECTION_TTL_MS = 5000L
        private val recentAutoLaunches = mutableSetOf<LaunchKey>()

        private fun cleanupStale() {
            val cutoff = System.currentTimeMillis() - LOOP_DETECTION_TTL_MS
            recentAutoLaunches.removeAll { it.timestamp < cutoff }
        }

        private fun hasRecentLaunch(url: String, packageName: String): Boolean {
            cleanupStale()
            return recentAutoLaunches.any { it.url == url && it.packageName == packageName }
        }

        private fun recordLaunch(url: String, packageName: String) {
            cleanupStale()
            recentAutoLaunches.add(LaunchKey(url, packageName, System.currentTimeMillis()))
        }
    }

    private val initialIntent = MutableStateFlow<Intent?>(null)
    private val latestNewIntent = MutableStateFlow<Intent?>(null)

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d(BottomSheetActivity::class.simpleName, "Received result for $result")
        if (result.resultCode == RESULT_OK || latestNewIntent.value == null) {
            finish()
        } else {
            showToast(
                textId = R.string.bottom_sheet__event_app_refusal,
                duration = Toast.LENGTH_LONG,
                uiThread = true
            )
        }
    }
    private val launchHandler = LaunchHandler(launcher)

    val editorLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != RESULT_OK || result.data == null) return@registerForActivityResult

        val intent = result.data
            ?.getStringExtra(TextEditorActivity.EXTRA_TEXT)
            ?.toUri()
            ?.let { StandardIntents.createSelfIntent(it) }

        onNewIntent(intent!!)
    }

    private val intentFlow = MutableStateFlow(initialIntent.value)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.resolveResultFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .mapNotNull(::maybeHandleResult)
                .collectLatest(::handleLaunch)
        }

        lifecycleScope.launch {
            viewModel.warmupAsync()
        }

        setInitialIntent(intent)
        setContent(edgeToEdge = true) {
            AppTheme { Wrapper() }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Wrapper() {
        val event by viewModel.events.collectOnIO()
        val interaction by viewModel.interactions.collectOnIO()

        val resolveResult by viewModel.resolveResultFlow.collectAsStateWithLifecycle()
        val currentIntent by intentFlow.collectAsStateWithLifecycle()

        val coroutineScope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState()

        LaunchedEffect(key1 = resolveResult) {
            logger.debug("Expanding bottom sheet, status: $resolveResult, isPending=${resolveResult == IntentResolveResult.Pending}")
            if (resolveResult != IntentResolveResult.Pending) {
                if (viewModel.expandFully.value) {
                    sheetState.expand()
                } else {
                    sheetState.partialExpand()
                }
            }
        }

        val controller = remember {
            DefaultBottomSheetStateController(
                activity = this@BottomSheetActivity,
                editorLauncher = editorLauncher,
                coroutineScope = coroutineScope,
                drawerState = sheetState,
                onNewIntent = ::onNewIntent,
                dispatch = { interaction ->
                    coroutineScope.launch {
                        (resolveResult as? IntentResolveResult.Default)
                            ?.let { viewModel.handle(this@BottomSheetActivity, it, interaction) }
                            ?.let { handleLaunch(it) }
                    }
                }
            )
        }

        val themeAmoled by viewModel.themeAmoled.collectAsStateWithLifecycle()
        val interceptAccidentalTaps by viewModel.interceptAccidentalTaps.collectAsStateWithLifecycle()
        val debug by LocalUiDebug.current.drawBorders.collectAsStateWithLifecycle()
        
        StandardBottomSheet(
            onDismissRequest = { finish() },
            sheetState = sheetState,
            containerColor = if (themeAmoled) Color.Black else MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(
                topStart = 22.0.dp,
                topEnd = 22.0.dp,
                bottomEnd = 0.0.dp,
                bottomStart = 0.0.dp
            ),
            modifier = Modifier
                .interceptTaps(sheetState, interceptAccidentalTaps)
                .debugBorder(debug, 1.dp, Color.Red)
        ) {
            Box(Modifier.fillMaxWidth()) {
                SheetContent(resolveResult, Modifier, event, interaction, coroutineScope, sheetState, controller)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SheetContent(
        resolveResult: IntentResolveResult,
        modifier: Modifier,
        event: ResolveEvent,
        interaction: ResolverInteraction,
        coroutineScope: CoroutineScope,
        sheetState: SheetState,
        controller: BottomSheetStateController,
    ) {
        when (resolveResult) {
            is IntentResolveResult.Pending -> {
                LoadingIndicatorWrapper(
                    event = event,
                    interaction = interaction,
                    requestExpand = {
                        coroutineScope.launch {
                            sheetState.expand()
                        }
                    }
                )
            }

            is IntentResolveResult.Default -> {
                val enableIgnoreLibRedirectButton by viewModel.enableIgnoreLibRedirectButton.collectAsStateWithLifecycle()
                val bottomSheetProfileSwitcher by viewModel.bottomSheetProfileSwitcher.collectAsStateWithLifecycle()
                val urlCopiedToast by viewModel.urlCopiedToast.collectAsStateWithLifecycle()
                val downloadStartedToast by viewModel.downloadStartedToast.collectAsStateWithLifecycle()
                val hideAfterCopying by viewModel.hideAfterCopying.collectAsStateWithLifecycle()
                val bottomSheetNativeLabel by viewModel.bottomSheetNativeLabel.collectAsStateWithLifecycle()
                val gridLayout by viewModel.gridLayout.collectAsStateWithLifecycle()
                val previewUrl by viewModel.previewUrl.collectAsStateWithLifecycle()
                val hideBottomSheetChoiceButtons by viewModel.hideBottomSheetChoiceButtons.collectAsStateWithLifecycle()
                val alwaysShowPackageName by viewModel.alwaysShowPackageName.collectAsStateWithLifecycle()
                val manualFollowRedirects by viewModel.manualFollowRedirects.collectAsStateWithLifecycle()
                val doubleTapUrl by viewModel.doubleTapUrl.collectAsStateWithLifecycle()

                BottomSheetApps(
                    modifier = modifier,
                    result = resolveResult,
                    imageLoader = viewModel.imageLoader,
                    enableIgnoreLibRedirectButton = enableIgnoreLibRedirectButton,
                    enableSwitchProfile = bottomSheetProfileSwitcher,
                    profileSwitcher = viewModel.profileSwitcher,
                    enableUrlCopiedToast = urlCopiedToast,
                    enableDownloadStartedToast = downloadStartedToast,
                    enableManualRedirect = manualFollowRedirects,
                    hideAfterCopying = hideAfterCopying,
                    bottomSheetNativeLabel = bottomSheetNativeLabel,
                    gridLayout = gridLayout,
                    appListSelectedIdx = viewModel.appListSelectedIdx.intValue,
                    copyUrl = { label, url ->
                        viewModel.clipboardManager.setText(label, url)
                    },
                    startDownload = { url, downloadable ->
                        viewModel.startDownload(resources, url, downloadable)
                    },
                    isPrivateBrowser = ::isPrivateBrowser,
                    showToast = { textId, duration, _ ->
                        coroutineScope.launch { showToast(textId = textId, duration = duration) }
                    },
                    controller = controller,
                    showPackage = alwaysShowPackageName,
                    previewUrl = previewUrl,
                    hideBottomSheetChoiceButtons = hideBottomSheetChoiceButtons,
                    urlCardDoubleTap = doubleTapUrl
                )
            }

            is IntentResolveResult.IntentParseFailed -> {
                FailureSheetContentWrapper(
                    modifier = modifier,
                    exception = resolveResult.exception,
                    onShareClick = {},
                    onCopyClick = {},
                    onSearchClick = {}
                )
            }

            else -> {}
        }
    }

    private suspend fun showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
        val text = getString(textId)
        withContext(Dispatchers.Main) {
            Toast.makeText(this@BottomSheetActivity, text, duration).show()
        }
    }

    private suspend fun isPrivateBrowser(hasUri: Boolean, info: ActivityAppInfo): Browser? {
        if (!viewModel.enableRequestPrivateBrowsingButton.value || !hasUri) return null
        return viewModel.isAllowedKnownBrowser(info.componentName, privateOnly = true)
    }

    private suspend fun maybeHandleResult(result: IntentResolveResult?): LaunchIntent? {
        return when (result) {
            is IntentResolveResult.Default if result.hasAutoLaunchApp && result.app != null -> {
                val url = result.uri?.toString()
                val packageName = result.app?.packageName
                if (url != null && packageName != null && hasRecentLaunch(url, packageName)) return null
                if (url != null && packageName != null) recordLaunch(url, packageName)
                viewModel.makeOpenAppIntent(
                    result.app,
                    result.intent,
                    referrer,
                    result.isRegularPreferredApp,
                    null,
                    false
                )
            }
            is IntentResolveResult.IntentResult -> LaunchRawIntent(result.intent)
            else -> null
        }
    }

    private suspend fun handleLaunch(intent: LaunchIntent) {
        val result = launchHandler.start(intent.intent)
        if (result !is LaunchFailure) return

        logger.error("Launch failed: $result", result.ex)
        val textId = when (result) {
            is LaunchResult.Illegal -> R.string.bottom_sheet__text_launch_illegal
            is LaunchResult.NotAllowed -> R.string.bottom_sheet__text_launch_not_allowed
            is LaunchResult.Other -> R.string.bottom_sheet__text_launch_failure_other
            is LaunchResult.Unknown -> R.string.bottom_sheet__text_launch_failure_unknown
            is LaunchResult.NotFound -> R.string.resolve_activity_failure
        }

        showToast(textId)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    fun setInitialIntent(intent: Intent) {
        initialIntent.tryEmit(intent)
        latestNewIntent.tryEmit(null)
        viewModel.resolveAsync(intent.toSafeIntent(), referrer)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        latestNewIntent.tryEmit(intent)
        viewModel.resolveAsync(intent.toSafeIntent(), referrer)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (!viewModel.noBottomSheetStateSave.value) {
            super.onSaveInstanceState(outState)
        }
    }
}
