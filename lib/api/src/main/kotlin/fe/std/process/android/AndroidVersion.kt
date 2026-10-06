package fe.std.process.android

import android.os.Build

public object AndroidVersion {
    public const val P: Int = 28
    public const val Q: Int = 29
    public const val R: Int = 30
    public const val S: Int = 31
    public const val T: Int = 33
    public const val U: Int = 34

    public val SDK_INT: Int get() = Build.VERSION.SDK_INT

    public fun isAtLeastApi28P(): Boolean = SDK_INT >= P
    public fun isAtLeastApi29Q(): Boolean = SDK_INT >= Q
    public fun isAtLeastApi30R(): Boolean = SDK_INT >= R
    public fun isAtLeastApi31S(): Boolean = SDK_INT >= S
    public fun isAtLeastApi33T(): Boolean = SDK_INT >= T
    public fun isAtLeastApi34U(): Boolean = SDK_INT >= U

    public fun atLeastApi(api: Int, block: () -> Unit) {
        if (SDK_INT >= api) block()
    }
}
