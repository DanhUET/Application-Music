package pro.branium.infrastructure.mapper.local

import pro.branium.core_database.entity.playlist.PlaylistEntity
import pro.branium.core_model.PlaylistModel


fun PlaylistModel.toEntity(): PlaylistEntity {
    return PlaylistEntity(
        playlistId = playlistId,
        name = name,
        artwork = artwork,
        createdAt = createdAt
    )
}

fun PlaylistEntity.toModel(): PlaylistModel {
    return PlaylistModel(
        playlistId = playlistId,
        name = name,
        artwork = artwork,
        createdAt = createdAt
    )
}

fun List<PlaylistEntity>.toModels(): List<PlaylistModel> {
    return map { it.toModel() }
}

fun List<PlaylistModel>.toEntities(): List<PlaylistEntity> {
    return map { it.toEntity() }
}
