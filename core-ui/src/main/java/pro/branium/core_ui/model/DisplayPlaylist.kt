package pro.branium.core_ui.model


data class DisplayPlaylist(
    val playlistId: Long,
    val name: String,
    val artwork: String?,
    val createdAt: Long,
    val isFavorite: Boolean = false,
    val songs: List<DisplaySongModel> = emptyList()
)