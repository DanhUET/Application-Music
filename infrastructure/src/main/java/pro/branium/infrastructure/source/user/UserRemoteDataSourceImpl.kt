package pro.branium.infrastructure.source.user

import kotlinx.coroutines.flow.Flow
import pro.branium.core_network.dto.user.UserDto
import pro.branium.feature_user.data.datasource.UserRemoteDataSource
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor() : UserRemoteDataSource {
    override fun getUserInfo(userId: Long): Flow<UserDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun login(userModel: UserDto): UserDto? {
        TODO("Not yet implemented")
    }

    override suspend fun update(userModel: UserDto): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun register(userModel: UserDto): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userModel: UserDto): Boolean {
        TODO("Not yet implemented")
    }
}