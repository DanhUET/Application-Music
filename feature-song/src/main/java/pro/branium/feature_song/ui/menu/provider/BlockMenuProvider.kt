package pro.branium.feature_song.ui.menu.provider

import pro.branium.core_resources.R
import pro.branium.core_model.SongModel
import pro.branium.core_model.action.SongAction
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.core_ui.menu.SongMenuProvider
import javax.inject.Inject

class BlockMenuProvider @Inject constructor() : SongMenuProvider {
    override fun provide(song: SongModel): SongMenuItem {
        return SongMenuItem(
            id = "block",
            textRes = R.string.item_block,
            icon = R.drawable.ic_block,
            stateIcon = null,
            action = SongAction.Block(song.id)
        )
    }
}