package pro.branium.infrastructure.mapper.remote

import pro.branium.core_network.dto.user.UserDto
import pro.branium.feature_user.domain.model.UserModel

fun UserDto.toModel(): UserModel {
    return UserModel(
        userId = this.userId,
        username = this.username,
        password = this.password,
        email = this.email,
        phoneNumber = this.phoneNumber,
        createdAt = this.createdAt,
        avatar = this.avatar
    )
}

fun UserModel.toDto(): UserDto {
    return UserDto(
        userId = this.userId,
        username = this.username,
        password = this.password,
        email = this.email,
        phoneNumber = this.phoneNumber,
        createdAt = this.createdAt,
        avatar = this.avatar
    )
}

fun List<UserDto>.toModels(): List<UserModel> {
    return this.map { it.toModel() }
}

fun List<UserModel>.toDtos(): List<UserDto> {
    return this.map { it.toDto() }
}