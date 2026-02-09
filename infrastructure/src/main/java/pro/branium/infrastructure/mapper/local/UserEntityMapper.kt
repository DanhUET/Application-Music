package pro.branium.infrastructure.mapper.local

import pro.branium.core_database.entity.user.UserEntity
import pro.branium.feature_user.domain.model.UserModel

fun UserModel.toEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        username = this.username,
        password = this.password,
        email = this.email,
        phoneNumber = this.phoneNumber,
        createdAt = this.createdAt,
        avatar = this.avatar
    )
}

fun UserEntity.toModel(): UserModel {
    return UserModel(
        userId = this.userId,
        username = this.username,
        password = this.password ?: "",
        email = this.email,
        phoneNumber = this.phoneNumber ?: "",
        avatar = this.avatar,
        createdAt = this.createdAt
    )
}

fun List<UserEntity>.toModels(): List<UserModel> {
    return this.map { it.toModel() }
}

fun List<UserModel>.toEntities(): List<UserEntity> {
    return this.map { it.toEntity() }
}
