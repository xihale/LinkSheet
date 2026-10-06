package app.linksheet.feature.browser.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.linksheet.compose.list.item.PreferenceDividedSwitchListItem
import app.linksheet.compose.page.SaneScaffoldSettingsPage
import app.linksheet.feature.browser.R
import app.linksheet.feature.browser.viewmodel.PrivateBrowsingSettingsViewModel
import fe.android.compose.text.StringResourceContent.Companion.textContent
import fe.composekit.component.ContentType
import fe.composekit.component.list.column.shape.SelectableShapeListItem
import fe.composekit.component.list.item.ContentPosition
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PrivateBrowsingSettings(
    onBackPressed: () -> Unit,
    navigateAllowedBrowsers: () -> Unit,
    viewModel: PrivateBrowsingSettingsViewModel = koinViewModel(),
) {
    val allowedBrowsers by viewModel.allowedBrowsers.collectAsStateWithLifecycle(initialValue = emptySet())

    PrivateBrowsingSettingsRouteInternal(
        statePreference = viewModel.enabled,
        enabledCount = allowedBrowsers.size,
        navigateAllowedBrowsers = navigateAllowedBrowsers,
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun PrivateBrowsingSettingsRouteInternal(
    statePreference: fe.composekit.preference.ViewModelStatePreference<Boolean, Boolean, fe.android.preference.helper.Preference.Default<Boolean>>,
    enabledCount: Int,
    navigateAllowedBrowsers: () -> Unit,
    onBackPressed: () -> Unit,
) {
    SaneScaffoldSettingsPage(
        headline = stringResource(id = R.string.settings_private_browsing__title_private_browsing),
        onBackPressed = onBackPressed,
    ) {
        item(key = R.string.settings_private_browsing__title_enable_private_browsing, contentType = ContentType.SingleGroupItem) {
            PreferenceDividedSwitchListItem(
                statePreference = statePreference,
                onContentClick = onBackPressed,
                headlineContent = textContent(R.string.settings_private_browsing__title_enable_private_browsing),
                supportingContent = textContent(R.string.enable_request_private_browsing_button_explainer),
            )
        }

        divider(id = R.string.settings_private_browsing__divider_configuration)

        item(key = R.string.settings_private_browsing__title_allowed_browsers, contentType = ContentType.SingleGroupItem) {
            SelectableShapeListItem(
                headlineContent = textContent(R.string.settings_private_browsing__title_allowed_browsers),
                supportingContent = textContent(
                    id = R.string.settings_private_browsing__text_allowed_browsers, enabledCount
                ),
                position = ContentPosition.Trailing,
                onClick = navigateAllowedBrowsers,
            )
        }
    }
}
