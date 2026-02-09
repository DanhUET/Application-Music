package pro.branium.infrastructure.mapper.local

import pro.branium.core_database.entity.album.AlbumEntity
import pro.branium.core_database.entity.album.AlbumWithSongs
import pro.branium.core_model.AlbumModel

fun AlbumModel.toEntity(): AlbumEntity {
    return AlbumEntity(
        id = this.id,
        name = this.name,
        artistName = this.artistName,
        releaseDate = this.releaseDate,
        genre = this.genre,
        albumType = this.albumType,
        playCount = this.playCount,
        artworkUrl = this.artwork
    )
}

fun AlbumEntity.toModel(): AlbumModel {
    return AlbumModel(
        id = this.id,
        name = this.name,
        artistName = this.artistName,
        releaseDate = this.releaseDate,
        genre = this.genre,
        albumType = this.albumType,
        playCount = this.playCount,
        artwork = this.artworkUrl
    )
}

fun AlbumWithSongs.toModel(): AlbumModel? {
    val albumEntity = this.album ?: return null
    return AlbumModel(
        id = albumEntity.id,
        name = albumEntity.name,
        artistName = albumEntity.artistName,
        releaseDate = albumEntity.releaseDate,
        genre = albumEntity.genre,
        albumType = albumEntity.albumType,
        playCount = albumEntity.playCount,
        artwork = albumEntity.artworkUrl,
        songs = this.songs.map { songEntity ->
            songEntity.toModel()
        }
    )
}

fun List<AlbumModel>.toEntities(): List<AlbumEntity> {
    return this.map { it.toEntity() }
}

fun List<AlbumEntity>.toModels(): List<AlbumModel> {
    return this.map { it.toModel() }
}