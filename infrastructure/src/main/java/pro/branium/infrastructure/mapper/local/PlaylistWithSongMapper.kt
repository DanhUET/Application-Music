package pro.branium.infrastructure.mapper.local

import pro.branium.core_database.entity.playlist.PlaylistWithSongs
import pro.branium.core_model.PlaylistModel


fun PlaylistWithSongs.toModel(): PlaylistModel {
    return PlaylistModel(
        playlist.playlistId,
        playlist.name,
        playlist.artwork,
        playlist.createdAt,
        songs.songEntitiesToModels()
    )
}

fun List<PlaylistWithSongs>.toModels(): List<PlaylistModel> {
    return map { it.toModel() }
}