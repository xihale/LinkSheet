package app.linksheet.feature.downloader

import app.linksheet.feature.downloader.DownloadCheckResult.Downloadable
import fe.linksheet.util.mime.KnownMimeTypes
import fe.linksheet.util.mime.MimeType
import fe.std.uri.StdUrl
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.dsl.module

public val DownloaderModule: org.koin.core.module.Module = module {
    single<Downloader> {
        Downloader(client = get())
    }
}

public sealed interface DownloadCheckResult {
    public class Downloadable(public val fileName: String, public val extension: String?) : DownloadCheckResult {
        public fun toFileName(): String = "$fileName.$extension"
    }

    public data object NonDownloadable : DownloadCheckResult
    public data object MimeTypeDetectionFailed : DownloadCheckResult
}

public fun DownloadCheckResult.isDownloadable(): Boolean = this is Downloadable

public class Downloader(
    private val client: HttpClient
) {
    public fun checkIsNonHtmlFileEnding(url: StdUrl): DownloadCheckResult {
        // Temporary stub during refactoring
        return DownloadCheckResult.MimeTypeDetectionFailed
    }

    public suspend fun isNonHtmlContentUri(url: StdUrl): DownloadCheckResult {
        // Temporary stub during refactoring
        return DownloadCheckResult.NonDownloadable
    }
}
