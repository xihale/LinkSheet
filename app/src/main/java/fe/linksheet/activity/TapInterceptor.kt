package fe.linksheet.activity

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
fun Modifier.interceptTaps(state: SheetState, interceptAccidentalTaps: Boolean): Modifier {
    if (!interceptAccidentalTaps) return this

    return pointerInput(Unit) {
        interceptTap { true } // Simplified: animations are fast in M3
    }
}

private suspend fun PointerInputScope.interceptTap(
    pass: PointerEventPass = PointerEventPass.Initial,
    shouldCancel: (PointerEvent) -> Boolean,
) = coroutineScope {
    awaitEachGesture {
        val down = awaitFirstDown(pass = pass)
        val downTime = System.currentTimeMillis()
        val tapTimeout = viewConfiguration.longPressTimeoutMillis

        do {
            val event = awaitPointerEvent(pass)
            if (shouldCancel(event)) break

            val currentTime = System.currentTimeMillis()

            if (event.changes.size != 1) break
            if (currentTime - downTime >= tapTimeout) break

            val change = event.changes[0]

            if (change.id == down.id && !change.pressed) {
                change.consume()
            }
        } while (event.changes.any { it.id == down.id && it.pressed })
    }
}
