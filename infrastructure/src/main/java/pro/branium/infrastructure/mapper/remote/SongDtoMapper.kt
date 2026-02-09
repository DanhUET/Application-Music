package pro.branium.infrastructure.mapper.remote

import pro.branium.core_model.SongModel
import pro.branium.core_network.dto.song.SongDto

fun SongDto.toModel(): SongModel {
    return SongModel(
        id = this.id,
        title = this.title,
        album = this.album,
        artist = this.artist.ifEmpty { "Unknown" },
        source = this.source,
        artworkUrl = this.artworkUrl,
        durationSeconds = this.durationSeconds,
        counter = this.counter,
        trackNumber = this.trackNumber,
        genre = this.genre,
        year = this.year,
        lyricsUrl = this.lyricsUrl
    )
}

fun SongModel.toDto(): SongDto {
    return SongDto(
        id = this.id,
        title = this.title,
        album = this.album,
        artist = this.artist,
        source = this.source,
        artworkUrl = this.artworkUrl,
        durationSeconds = this.durationSeconds,
        counter = this.counter,
        trackNumber = this.trackNumber,
        genre = this.genre,
        year = this.year,
        lyricsUrl = this.lyricsUrl
    )
}

fun List<SongModel>.toDtos(): List<SongDto> {
    return this.map { it.toDto() }
}

fun List<SongDto>.toModels(): List<SongModel> {
    return this.map { it.toModel() }
}
