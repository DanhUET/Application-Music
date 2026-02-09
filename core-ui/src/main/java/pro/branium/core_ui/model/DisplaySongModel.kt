package pro.branium.core_ui.model

import pro.branium.core_resources.R
import pro.branium.core_model.SongModel

data class DisplaySongModel(
    val song: SongModel,
    val isFavoriteByUser: Boolean = false,
    val playingPosition: Long = 0,
    val isPlaying: Boolean = false,
    val isNowPlaying: Boolean = false,
    val playingState: Int? = R.drawable.ic_play_circle
)