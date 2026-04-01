package fe.android.preference.helper

import kotlin.reflect.KClass

sealed interface Preference<T : Any, NT> {
    val key: String
    val default: NT

    interface Default<T : Any> : Preference<T, T>
    interface Nullable<T : Any> : Preference<T, T?>
    interface Mapped<T : Any, M : Any> : Preference<T, T>

    interface Boolean : Default<kotlin.Boolean>
}

interface TypeMapper<T : Any, M : Any> {
    fun map(value: T): M
    fun unmap(value: M): T
}

open class EnumTypeMapper<T : Enum<T>>(private val entries: Array<T>) : TypeMapper<T, String> {
    override fun map(value: T): String = value.name
    override fun unmap(value: String): T = entries.first { it.name == value }
}

open class OptionTypeMapper<T : Any, M : Any>(
    private val mapper: (T) -> M,
    private val entriesProvider: () -> Array<T>
) : TypeMapper<T, M> {
    override fun map(value: T): M = mapper(value)
    override fun unmap(value: M): T = entriesProvider().first { mapper(it) == value }
}
