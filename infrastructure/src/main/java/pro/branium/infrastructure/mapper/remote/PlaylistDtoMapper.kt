package pro.branium.infrastructure.mapper.remote

import pro.branium.core_model.PlaylistModel
import pro.branium.core_network.dto.playlist.PlaylistDto

fun PlaylistModel.toDto(): PlaylistDto {
    return PlaylistDto(
        playlistId = playlistId,
        name = name,
        artwork = artwork,
        createdAt = createdAt
    )
}

fun PlaylistDto.toModel(): PlaylistModel {
    return PlaylistModel(
        playlistId = playlistId,
        name = name,
        artwork = artwork,
        createdAt = createdAt
    )
}

fun List<PlaylistDto>.toModels(): List<PlaylistModel> {
    return map { it.toModel() }
}

fun List<PlaylistModel>.toDtos(): List<PlaylistDto> {
    return map { it.toDto() }
}
