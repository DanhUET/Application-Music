package pro.branium.feature_user.domain.repository

import kotlinx.coroutines.flow.Flow
import pro.branium.feature_user.domain.model.UserModel

interface UserRepository {
    fun getUserInfo(userId: Int): Flow<UserModel?>
    suspend fun login(userModel: UserModel): UserModel?
    suspend fun register(userModel: UserModel) : Boolean
    suspend fun updateUser(userModel: UserModel): Boolean
    suspend fun countUser(): Int
    suspend fun deleteUser(userModel: UserModel): Boolean
}