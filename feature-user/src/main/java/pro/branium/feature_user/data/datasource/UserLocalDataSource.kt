package pro.branium.feature_user.data.datasource

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.user.UserEntity

interface UserLocalDataSource {
    fun getUserInfo(userId: Int): Flow<UserEntity?>

    suspend fun login(userModel: UserEntity): UserEntity?

    suspend fun update(userModel: UserEntity): Boolean

    suspend fun countUser(): Int

    suspend fun register(userModel: UserEntity): Boolean

    suspend fun deleteUser(userModel: UserEntity): Boolean
}