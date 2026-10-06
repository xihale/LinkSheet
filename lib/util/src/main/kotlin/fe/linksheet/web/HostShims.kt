package fe.linksheet.web

import fe.std.uri.StdUrl
import android.net.CompatUriHost

public val StdUrl.compatHost: CompatUriHost get() = CompatUriHost(host)
