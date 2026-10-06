package app.linksheet.feature.browser.navigation

import androidx.annotation.Keep
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import app.linksheet.compose.navigation.NavSubGraph
import app.linksheet.compose.util.animatedComposable
import app.linksheet.feature.browser.ui.PrivateBrowsingBrowsersSettings
import app.linksheet.feature.browser.ui.PrivateBrowsingSettings
import kotlinx.serialization.Serializable

@Serializable
object PrivateBrowsingNavSubGraph : NavSubGraph<PrivateBrowsingRoute> {
    override val startDestination = PrivateBrowsingRoute
    override val graph: NavGraphBuilder.(NavHostController) -> Unit = { navController ->
        animatedComposable<PrivateBrowsingRoute> {
            PrivateBrowsingSettings(
                onBackPressed = navController::popBackStack,
                navigateAllowedBrowsers = { navController.navigate(PrivateBrowserBrowserRoute) },
            )
        }

        animatedComposable<PrivateBrowserBrowserRoute> {
            PrivateBrowsingBrowsersSettings(onBackPressed = navController::popBackStack)
        }
    }
}

@Keep
@Serializable
data object PrivateBrowsingRoute

@Keep
@Serializable
data object PrivateBrowserBrowserRoute
