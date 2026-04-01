package fe.kotlin.extension.string

public fun String.substringOrNull(startIndex: Int, endIndex: Int): String? {
    return if (startIndex in 0..length && endIndex in startIndex..length) {
        substring(startIndex, endIndex)
    } else null
}
