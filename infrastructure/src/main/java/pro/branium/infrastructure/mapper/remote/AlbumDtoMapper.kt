package pro.branium.infrastructure.mapper.remote

import pro.branium.core_model.AlbumModel
import pro.branium.core_network.dto.album.AlbumDto
import pro.branium.feature_album.domain.model.AlbumSummaryModel


fun AlbumDto.toModel(): AlbumModel? {
    if (this.id == null) return null
    return AlbumModel(
        id = this.id ?: 0,
        name = this.name.toString(),
        artistName = this.artistName.toString(),
        genre = this.genre.toString(),
        releaseDate = this.releaseDate ?: 0,
        albumType = this.albumType.toString(),
        playCount = this.playCount ?: 0,
        artwork = this.artworkUrl
    )
}

fun AlbumModel.toDto(): AlbumDto {
    return AlbumDto(
        id = this.id,
        name = this.name,
        artistName = this.artistName,
        genre = this.genre,
        releaseDate = this.releaseDate,
        albumType = this.albumType,
        playCount = this.playCount,
        artworkUrl = this.artwork
    )
}

fun AlbumDto.toAlbumSummaryModel(): AlbumSummaryModel {
    return AlbumSummaryModel(
        albumId = this.id ?: 0,
        albumName = this.name.toString(),
        artistName = this.artistName.toString(),
        artworkUrl = this.artworkUrl
    )
}

fun List<AlbumDto>.toModels(): List<AlbumModel> {
    return this.mapNotNull { it.toModel() }
}

fun List<AlbumModel>.toDtos(): List<AlbumDto> {
    return this.map { it.toDto() }
}

fun List<AlbumDto>.toAlbumSummaryModels(): List<AlbumSummaryModel> {
    return this.map { it.toAlbumSummaryModel() }
}
