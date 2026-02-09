package pro.branium.core_model

data class PlaylistModel(
    val playlistId: Long = -1,
    val name: String = "",
    val artwork: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val songs: List<SongModel> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistModel

        return playlistId == other.playlistId
    }

    override fun hashCode(): Int {
        return playlistId.hashCode()
    }
}