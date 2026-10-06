package app.linksheet.feature.browser.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import app.linksheet.compose.list.item.PreferenceDividedSwitchListItem
import app.linksheet.feature.browser.R
import fe.android.compose.text.StringResourceContent.Companion.textContent
import fe.android.preference.helper.Preference
import fe.composekit.component.CommonDefaults
import fe.composekit.component.shape.CustomShapeDefaults
import fe.composekit.layout.column.SaneLazyListScope
import fe.composekit.preference.ViewModelStatePreference

fun SaneLazyListScope.privateBrowsingListItem(
    statePreference: ViewModelStatePreference<Boolean, Boolean, Preference.Default<Boolean>>,
    onClick: () -> Unit,
) {
    item(key = R.string.settings_private_browsing__title_enable_private_browsing) {
        PrivateBrowsingListItem(
            shape = CustomShapeDefaults.SingleShape,
            padding = CommonDefaults.EmptyPadding,
            statePreference = statePreference,
            onClick = onClick,
        )
    }
}

@Composable
fun PrivateBrowsingListItem(
    shape: Shape = CustomShapeDefaults.SingleShape,
    padding: PaddingValues = CommonDefaults.EmptyPadding,
    statePreference: ViewModelStatePreference<Boolean, Boolean, Preference.Default<Boolean>>,
    onClick: () -> Unit,
) {
    PreferenceDividedSwitchListItem(
        shape = shape,
        padding = padding,
        statePreference = statePreference,
        onContentClick = onClick,
        headlineContent = textContent(R.string.settings_private_browsing__title_enable_private_browsing),
        supportingContent = textContent(R.string.enable_request_private_browsing_button_explainer),
    )
}
