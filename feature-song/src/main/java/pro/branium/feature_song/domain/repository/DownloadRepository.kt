package pro.branium.feature_song.domain.repository

interface DownloadRepository {
    fun isDownloaded(songId: String): Boolean
    fun download(songId: String)
}