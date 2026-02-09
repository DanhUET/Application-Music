package pro.branium.core_ui.mapper

import pro.branium.core_model.PlaylistModel
import pro.branium.core_ui.model.DisplayPlaylist


fun PlaylistModel.toDisplayPlaylist(): DisplayPlaylist {
    return DisplayPlaylist(
        playlistId = playlistId,
        name = name,
        artwork = artwork,
        createdAt = createdAt,
        isFavorite = false,
        songs = songs.toDisplayModels()
    )
}

fun DisplayPlaylist.toModel(): PlaylistModel {
    return PlaylistModel(
        playlistId = playlistId,
        name = name,
        artwork = artwork,
        createdAt = createdAt,
        songs = songs.toSongModels()
    )
}

fun List<PlaylistModel>.toDisplayPlaylist(): List<DisplayPlaylist> {
    return this.map { it.toDisplayPlaylist() }
}

fun List<DisplayPlaylist>.toSongModels(): List<PlaylistModel> {
    return this.map { it.toModel() }
}