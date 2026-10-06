package fe.linksheet.util

import android.content.pm.ResolveInfo
import android.content.ComponentName
import fe.std.result.IResult
import fe.std.result.asSuccess
import fe.std.result.tryCatch

// Collection shims
public inline fun <T, R> Iterable<T>.mapToSet(transform: (T) -> R): Set<R> {
    return map(transform).toSet()
}

// Android shims
public val ResolveInfo.componentName: ComponentName
    get() = ComponentName(activityInfo.packageName, activityInfo.name)
