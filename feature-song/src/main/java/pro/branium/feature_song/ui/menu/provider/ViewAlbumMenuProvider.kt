package pro.branium.feature_song.ui.menu.provider

import pro.branium.core_resources.R
import pro.branium.core_model.SongModel
import pro.branium.core_model.action.SongAction
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.core_ui.menu.SongMenuProvider
import javax.inject.Inject

class ViewAlbumMenuProvider @Inject constructor() : SongMenuProvider {
    override fun provide(song: SongModel): SongMenuItem {
        return SongMenuItem(
            id = "view_album",
            textRes = R.string.item_view_album,
            icon = R.drawable.ic_album_black,
            stateIcon = null,
            action = SongAction.ViewAlbum(0) // todo
        )
    }
}