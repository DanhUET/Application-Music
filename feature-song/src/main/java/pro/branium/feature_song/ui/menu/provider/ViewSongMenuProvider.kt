package pro.branium.feature_song.ui.menu.provider

import pro.branium.core_resources.R
import pro.branium.core_model.SongModel
import pro.branium.core_model.action.SongAction
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.core_ui.menu.SongMenuProvider
import javax.inject.Inject

class ViewSongMenuProvider @Inject constructor() : SongMenuProvider {
    override fun provide(song: SongModel): SongMenuItem {
        return SongMenuItem(
            id = "view_song",
            textRes = R.string.item_view_song_information,
            icon = R.drawable.ic_information,
            stateIcon = null,
            action = SongAction.ViewSong(song.id)
        )
    }
}