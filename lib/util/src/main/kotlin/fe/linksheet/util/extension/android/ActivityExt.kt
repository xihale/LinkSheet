package fe.linksheet.util.extension.android

import android.app.Activity
import fe.std.result.asSuccess
import fe.std.result.IResult

public fun Activity.safeFinish(): IResult<Unit> {
    finish()
    return Unit.asSuccess()
}
