package pro.branium.feature_song.ui.menu.provider

import pro.branium.core_resources.R
import pro.branium.core_model.SongModel
import pro.branium.core_model.action.SongAction
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.core_ui.menu.SongMenuProvider
import pro.branium.feature_song.domain.repository.DownloadRepository
import javax.inject.Inject

class DownloadMenuProvider @Inject constructor(
    private val repo: DownloadRepository
) : SongMenuProvider {
    override fun provide(song: SongModel): SongMenuItem {
        val isDownloaded = repo.isDownloaded(song.id)
        val titleId = if (isDownloaded)
            R.string.item_downloaded
        else
            R.string.item_download
        return SongMenuItem(
            id = "download",
            textRes = titleId,
            icon = R.drawable.ic_download,
            stateIcon = if (isDownloaded) R.drawable.ic_check else null,
            action = SongAction.Download(song.id)
        )
    }
}