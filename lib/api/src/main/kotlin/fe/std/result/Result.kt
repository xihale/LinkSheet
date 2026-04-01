package fe.std.result

public interface IResult<out T>

/**
 * Result shim to decouple from Grrfe library.
 */
public sealed class Result<out T> : IResult<T> {
    public data class Success<T>(val value: T) : Result<T>()
    public data class Failure(val exception: Throwable) : Result<Nothing>()

    public val isSuccess: kotlin.Boolean get() = this is Success
    public val isFailure: kotlin.Boolean get() = this is Failure

    public fun getOrNull(): T? = (this as? Success)?.value
    public fun exceptionOrNull(): Throwable? = (this as? Failure)?.exception

    public inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(value)
        return this
    }

    public inline fun onFailure(action: (Throwable) -> Unit): Result<T> {
        if (this is Failure) action(exception)
        return this
    }
    
    public companion object {
        public fun <T> success(value: T): Result<T> = Success(value)
        public fun failure(exception: Throwable): Result<Nothing> = Failure(exception)
    }
}

public inline fun <T> tryCatch(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

public inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(value))
        is Result.Failure -> Result.Failure(exception)
    }
}
