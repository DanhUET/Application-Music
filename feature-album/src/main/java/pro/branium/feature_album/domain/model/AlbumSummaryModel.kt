package pro.branium.feature_album.domain.model

data class AlbumSummaryModel(
    val albumId: Int,
    val albumName: String,
    val artistName: String,
    val artworkUrl: String?
)
