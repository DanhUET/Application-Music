package pro.branium.feature_song.ui.menu.provider

import pro.branium.core_resources.R
import pro.branium.core_model.SongModel
import pro.branium.core_model.action.SongAction
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.core_ui.menu.SongMenuProvider
import javax.inject.Inject

class ViewArtistMenuProvider @Inject constructor() : SongMenuProvider {
    override fun provide(song: SongModel): SongMenuItem {
        return SongMenuItem(
            id = "view_artist",
            textRes = R.string.item_view_artist,
            icon = R.drawable.ic_singer,
            stateIcon = null,
            action = SongAction.ViewArtist(0) // todo
        )
    }
}