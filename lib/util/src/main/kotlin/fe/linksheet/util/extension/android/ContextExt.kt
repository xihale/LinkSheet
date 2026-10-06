package fe.linksheet.util.extension.android

import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import fe.std.result.IResult
import fe.std.result.asSuccess
import fe.std.result.tryCatch

public fun Context.getCurrentLanguageTag(): String {
    return resources.configuration.locales[0].toLanguageTag()
}

public inline fun <reified T : Any> Context.getSystemServiceOrThrow(): T {
    return getSystemService<T>() ?: error("System service '${T::class.simpleName}' is not available")
}

public fun Context.tryStartActivity(intent: Intent): IResult<Unit> {
    return tryCatch {
        startActivity(intent)
        Unit.asSuccess()
    }
}

