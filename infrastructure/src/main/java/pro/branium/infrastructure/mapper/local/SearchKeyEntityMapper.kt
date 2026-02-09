package pro.branium.infrastructure.mapper.local

import pro.branium.feature.search.data.entity.HistorySearchedKeyEntity
import pro.branium.feature.search.domain.model.HistorySearchedKeyModel


fun HistorySearchedKeyModel.toEntity(): HistorySearchedKeyEntity {
    return HistorySearchedKeyEntity(id = this.id, key = this.key)
}

fun HistorySearchedKeyEntity.toModel(): HistorySearchedKeyModel {
    return HistorySearchedKeyModel(id = this.id, key = this.key)
}

fun List<HistorySearchedKeyEntity>.toModels(): List<HistorySearchedKeyModel> {
    return this.map { it.toModel() }
}

fun List<HistorySearchedKeyModel>.toEntities(): List<HistorySearchedKeyEntity> {
    return this.map { it.toEntity() }
}