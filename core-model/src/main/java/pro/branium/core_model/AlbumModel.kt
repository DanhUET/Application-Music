package pro.branium.core_model

data class AlbumModel(
    val id: Int = 0,
    val name: String,
    val artistName: String,
    val releaseDate: Long,
    val genre: String,
    val albumType: String,
    val playCount: Int = 0,
    val songs: List<SongModel> = emptyList(),
    val artwork: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AlbumModel) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    val trackCount: Int
        get() = songs.size
}