package pro.branium.core_ui.mapper

import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.core_model.SongModel


fun DisplaySongModel.toModel(): SongModel {
    return this.song
}

fun SongModel.toDisplayModel(
    isFavorite: Boolean = false,
    playingPosition: Long = 0
): DisplaySongModel {
    return DisplaySongModel(
        this,
        isFavorite,
        playingPosition,
        isNowPlaying = false,
        isPlaying = false
    )
}

fun List<DisplaySongModel>.toSongModels(): List<SongModel> {
    return this.map { it.song }
}

fun List<SongModel>.toDisplayModels(): List<DisplaySongModel> {
    return this.map { it.toDisplayModel() }
}
