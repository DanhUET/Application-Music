package pro.branium.core_model

data class PlaylistSummary(
    val playlistId: Long,
    val name: String,
    val artwork: String?,
    val songCount: Int
)
