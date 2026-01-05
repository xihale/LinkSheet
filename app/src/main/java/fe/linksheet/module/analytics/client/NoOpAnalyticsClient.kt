package fe.linksheet.module.analytics.client

import android.content.Context
import fe.linksheet.module.analytics.AnalyticsClient
import fe.linksheet.module.analytics.AnalyticsEvent
import app.linksheet.lib.log.moz.log.logger.Logger

internal class NoOpAnalyticsClient : AnalyticsClient(Logger("NoOpAnalyticsClient")) {
    override fun setup(context: Context) {}
    override fun sendEvents(events: List<AnalyticsEvent>): Boolean = true
}
