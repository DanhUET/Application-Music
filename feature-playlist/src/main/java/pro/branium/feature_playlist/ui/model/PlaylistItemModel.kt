package pro.branium.feature_playlist.ui.model

data class PlaylistItemModel(
    val playlistId: Long,
    val name: String,
    val artwork: String?,
    val songCount: Int
)
