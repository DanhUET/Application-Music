package pro.branium.core_ui.menu

import pro.branium.core_model.SongModel

interface SongMenuProvider {
    fun provide(song: SongModel): SongMenuItem
}