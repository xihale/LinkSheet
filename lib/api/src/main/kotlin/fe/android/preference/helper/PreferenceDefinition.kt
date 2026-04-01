package fe.android.preference.helper

import kotlin.reflect.KClass

abstract class PreferenceDefinition(vararg keys: String) {
    private val _all = mutableMapOf<String, Preference<*, *>>()
    val all: Map<String, Preference<*, *>> get() = _all

    protected fun boolean(key: String, default: kotlin.Boolean = false): Preference.Boolean {
        return object : Preference.Boolean {
            override val key: String = key
            override val default: kotlin.Boolean = default
        }.also { _all[key] = it }
    }

    protected fun int(key: String, default: Int = 0): Preference.Default<Int> {
        return object : Preference.Default<Int> {
            override val key: String = key
            override val default: Int = default
        }.also { _all[key] = it }
    }

    protected fun long(key: String, default: Long = 0L): Preference.Default<Long> {
        return object : Preference.Default<Long> {
            override val key: String = key
            override val default: Long = default
        }.also { _all[key] = it }
    }

    protected fun string(key: String, default: String? = null): Preference.Nullable<String> {
        return object : Preference.Nullable<String> {
            override val key: String = key
            override val default: String? = default
        }.also { _all[key] = it }
    }

    protected fun string(key: String, defaultProvider: () -> String): Preference.Nullable<String> {
        return object : Preference.Nullable<String> {
            override val key: String = key
            override val default: String? = defaultProvider()
        }.also { _all[key] = it }
    }

    protected fun <T : Any, M : Any> mapped(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>,
        t: KClass<T>? = null,
        m: KClass<M>? = null
    ): Preference.Mapped<T, M> {
        return object : Preference.Mapped<T, M> {
            override val key: String = key
            override val default: T = default
        }.also { _all[key] = it }
    }

    protected fun finalize() {}
    
    interface MigrationScope {
        fun <T: Any, NT> Preference<T, NT>.migrate(block: (Any, T) -> Unit) {}
    }
    
    protected fun <T: Any, NT> Preference<T, NT>.migrate(block: (Any, T) -> Unit): Preference<T, NT> = this

    fun runMigrations(repository: Any) {}
}
