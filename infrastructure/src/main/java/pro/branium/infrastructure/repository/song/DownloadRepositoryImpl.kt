package pro.branium.infrastructure.repository.song

import pro.branium.feature_song.domain.repository.DownloadRepository
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor() : DownloadRepository {
    override fun download(songId: String) {

    }

    override fun isDownloaded(songId: String): Boolean {
        return false
    }
}