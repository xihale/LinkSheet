package app.linksheet.feature.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.linksheet.feature.app.core.IAppInfoBase
import fe.composekit.component.icon.AppIconImage
import app.linksheet.feature.app.ui.AppInfoIconDefaults.DefaultIconSize

object AppInfoIconDefaults {
    val DefaultIconSize = 32.dp
}

@Composable
fun AppInfoIcon(
    modifier: Modifier = Modifier,
    size: Dp = DefaultIconSize,
    appInfo: IAppInfoBase,
) {
    val icon = appInfo.icon
    if (icon != null) {
        AppIconImage(
            modifier = modifier,
            size = size,
            icon = icon,
            label = appInfo.label,
        )
    }
}

@Preview(showBackground = true, apiLevel = 31)
@Composable
private fun AppInfoIconPreview() {
}
