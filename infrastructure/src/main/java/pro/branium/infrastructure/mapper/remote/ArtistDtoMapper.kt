package pro.branium.infrastructure.mapper.remote

import pro.branium.core_model.ArtistModel
import pro.branium.feature_artist.data.dto.ArtistDto

fun ArtistModel.toDto(): ArtistDto {
    return ArtistDto(
        id = this.id,
        name = this.name,
        avatar = this.avatar,
        interested = this.interested
    )
}

fun ArtistDto.toModel(): ArtistModel {
    return ArtistModel(
        id = this.id,
        name = this.name,
        avatar = this.avatar,
        interested = this.interested
    )
}

fun List<ArtistDto>.toModels(): List<ArtistModel> {
    return this.map { it.toModel() }
}

fun List<ArtistModel>.toDtos(): List<ArtistDto> {
    return this.map { it.toDto() }
}
