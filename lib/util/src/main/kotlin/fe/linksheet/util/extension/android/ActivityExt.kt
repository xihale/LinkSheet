package fe.linksheet.util.extension.android

import android.app.Activity
import fe.std.result.IResult
import fe.std.result.Result

public fun Activity.safeFinish(): IResult<Unit> {
    finish()
    return Result.success(Unit)
}
