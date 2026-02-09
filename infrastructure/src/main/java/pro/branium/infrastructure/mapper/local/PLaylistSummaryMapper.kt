package pro.branium.infrastructure.mapper.local

import pro.branium.core_database.entity.playlist.PlaylistWithCountEntity
import pro.branium.core_model.PlaylistSummary

fun PlaylistWithCountEntity.toPlaylistSummary(): PlaylistSummary {
    return PlaylistSummary(
        playlistId = this.playlistId,
        name = this.name,
        artwork = this.artwork,
        songCount = this.songCount
    )
}

fun List<PlaylistWithCountEntity>.toPlaylistSummaries(): List<PlaylistSummary> {
    return map { it.toPlaylistSummary() }
}