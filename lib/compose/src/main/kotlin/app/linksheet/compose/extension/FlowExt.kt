package app.linksheet.compose.extension

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

/**
 * Clean standard Flow extensions.
 */
@Composable
public fun <T> Flow<T>.collectOnIO(): State<T?> {
    return this.flowOn(Dispatchers.IO).collectAsStateWithLifecycle(initialValue = null)
}
