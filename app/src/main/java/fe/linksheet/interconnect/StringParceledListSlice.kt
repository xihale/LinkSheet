package fe.linksheet.interconnect

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StringParceledListSlice(
    val list: List<String>,
) : Parcelable
