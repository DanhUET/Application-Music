package pro.branium.feature_user.data.datasource

import kotlinx.coroutines.flow.Flow
import pro.branium.core_network.dto.user.UserDto

interface UserRemoteDataSource {
    fun getUserInfo(userId: Long): Flow<UserDto?>

    suspend fun login(userModel: UserDto): UserDto?

    suspend fun update(userModel: UserDto): Boolean

    suspend fun register(userModel: UserDto): Boolean

    suspend fun deleteUser(userModel: UserDto): Boolean
}