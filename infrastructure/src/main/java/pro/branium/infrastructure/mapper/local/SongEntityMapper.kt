package pro.branium.infrastructure.mapper.local

import pro.branium.core_database.entity.song.SongEntity
import pro.branium.core_model.SongModel


fun SongEntity.toModel(): SongModel {
    return SongModel(
        id = this.id,
        title = this.title,
        album = this.album,
        artist = this.artist,
        source = this.source,
        artworkUrl = this.imageArtworkUrl,
        durationSeconds = this.durationSeconds,
        counter = this.counter,
        trackNumber = this.trackNumber,
        genre = this.genre,
        year = this.year,
        lyricsUrl = this.lyricsUrl
    )
}

fun SongModel.toEntity(): SongEntity {
    return SongEntity(
        id = this.id,
        title = this.title,
        album = this.album,
        artist = this.artist,
        source = this.source,
        imageArtworkUrl = this.artworkUrl ?: "",
        durationSeconds = this.durationSeconds,
        counter = this.counter,
        trackNumber = this.trackNumber ?: 0,
        genre = this.genre,
        year = this.year,
        lyricsUrl = this.lyricsUrl
    )
}

fun List<SongEntity>.songEntitiesToModels(): List<SongModel> {
    return this.map { it.toModel() }
}

fun List<SongModel>.toEntities(): List<SongEntity> {
    return this.map { it.toEntity() }
}
