package pro.branium.feature_song.ui.menu.provider

import pro.branium.core_resources.R
import pro.branium.core_model.SongModel
import pro.branium.core_model.action.SongAction
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.core_ui.menu.SongMenuProvider
import javax.inject.Inject

class PlaylistMenuProvider @Inject constructor() : SongMenuProvider {
    override fun provide(song: SongModel): SongMenuItem {
        return SongMenuItem(
            id = "add_to_playlist",
            textRes = R.string.item_add_to_playlist,
            icon = R.drawable.ic_playlist,
            stateIcon = null,
            action = SongAction.AddToPlaylist(song.id)
        )
    }
}