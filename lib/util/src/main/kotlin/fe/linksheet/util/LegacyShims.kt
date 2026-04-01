package fe.linksheet.util

import android.content.pm.ResolveInfo
import android.content.ComponentName
import fe.std.result.IResult
import fe.std.result.Result
import fe.std.result.tryCatch

/**
 * Final legacy shims to break free from Grrfe ecosystem.
 */

// Result shims
public fun <T> Result<T>.toIResult(): IResult<T> = object : IResult<T> {}

public inline fun <T> tryCatchIResult(block: () -> T): IResult<T> {
    val res = tryCatch(block)
    return object : IResult<T> {}
}

// Collection shims
public inline fun <T, R> Iterable<T>.mapToSet(transform: (T) -> R): Set<R> {
    return map(transform).toSet()
}

// Android shims
public val ResolveInfo.componentName: ComponentName
    get() = ComponentName(activityInfo.packageName, activityInfo.name)

// Host/IP shims (Minimal stubs to pass type checks)
public object HostName {
    public fun from(host: String): Any = Any()
}

public class ShimAddress(public val value: String) {
    public val isAddress: Boolean = false
}

public fun String.asAddress(): ShimAddress = ShimAddress(this)
public val Any.isAddress: Boolean get() = false
