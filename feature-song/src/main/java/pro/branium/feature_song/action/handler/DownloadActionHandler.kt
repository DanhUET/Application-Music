package pro.branium.feature_song.action.handler

import android.view.View
import pro.branium.core_model.action.SongAction
import pro.branium.feature_song.action.SongActionHandler
import pro.branium.feature_song.domain.repository.DownloadRepository
import javax.inject.Inject

class DownloadActionHandler @Inject constructor(
    private val downloadRepository: DownloadRepository
) : SongActionHandler {
    override suspend fun handle(action: SongAction, anchorView: View?) {
        if (action is SongAction.Download) {
            downloadRepository.download(action.songId)
        }
    }
}