package pro.branium.core_model

data class SongModel(
    val id: String,
    val title: String,
    val album: String? = null,
    val artist: String = "Unknown",
    val source: String,
    val artworkUrl: String? = null,
    val durationSeconds: Int = 0,
    val counter: Int = 0,
    val trackNumber: Int? = null,
    val genre: String? = null,
    val year: Int? = null,
    val lyricsUrl: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SongModel) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
