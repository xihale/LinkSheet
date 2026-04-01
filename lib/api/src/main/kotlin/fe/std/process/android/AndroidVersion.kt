package fe.std.process.android

/**
 * Android version shims to replace Grrfe.std.core.
 */
public object AndroidVersion {
    public const val P: Int = 28
    public const val Q: Int = 29
    public const val R: Int = 30
    public const val S: Int = 31
    public const val T: Int = 33
    public const val U: Int = 34
    
    public val SDK_INT: Int get() = 33 // Default fallback
}
