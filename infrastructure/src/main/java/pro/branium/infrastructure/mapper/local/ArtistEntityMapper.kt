package pro.branium.infrastructure.mapper.local

import pro.branium.core_model.ArtistModel
import pro.branium.feature_artist.data.entity.ArtistEntity


fun ArtistModel.toEntity(): ArtistEntity {
    return ArtistEntity(
        id = id,
        name = name,
        avatar = avatar,
        interested = interested
    )
}

fun ArtistEntity.toModel(): ArtistModel {
    return ArtistModel(
        id = id,
        name = name,
        avatar = avatar,
        interested = interested
    )
}

fun List<ArtistEntity>.toModels(): List<ArtistModel> {
    return map { it.toModel() }
}

fun List<ArtistModel>.toEntities(): List<ArtistEntity> {
    return map { it.toEntity() }
}