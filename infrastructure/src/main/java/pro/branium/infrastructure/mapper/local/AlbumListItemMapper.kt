package pro.branium.infrastructure.mapper.local

import pro.branium.core_database.entity.album.AlbumListItem
import pro.branium.feature_album.domain.model.AlbumSummaryModel


fun AlbumListItem.toAlbumSummaryModel(): AlbumSummaryModel {
    return AlbumSummaryModel(
        albumId = this.id,
        albumName = this.name,
        artistName = this.artistName,
        artworkUrl = this.artworkUrl
    )
}

fun List<AlbumListItem>.toAlbumSummaryModelList(): List<AlbumSummaryModel> {
    return this.map { it.toAlbumSummaryModel() }
}