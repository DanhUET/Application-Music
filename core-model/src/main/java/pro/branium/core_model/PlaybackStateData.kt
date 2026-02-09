package pro.branium.core_model

data class PlaybackStateData(
    val songId: String? = null,
    val playlistId: Long? = null,
    val position: Long = 0,
)