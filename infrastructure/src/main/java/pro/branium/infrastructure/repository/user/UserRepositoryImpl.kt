package pro.branium.infrastructure.repository.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import pro.branium.feature_user.data.datasource.UserLocalDataSource
import pro.branium.feature_user.data.datasource.UserRemoteDataSource
import pro.branium.feature_user.domain.model.UserModel
import pro.branium.feature_user.domain.repository.UserRepository
import pro.branium.infrastructure.mapper.local.toEntity
import pro.branium.infrastructure.mapper.local.toModel
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {
    override fun getUserInfo(userId: Int): Flow<UserModel?> {
        return localDataSource.getUserInfo(userId)
            .mapNotNull { data -> data?.toModel() }
    }

    override suspend fun login(userModel: UserModel): UserModel? {
        return localDataSource.login(userModel.toEntity())?.toModel()
    }

    override suspend fun updateUser(userModel: UserModel): Boolean {
        return localDataSource.update(userModel.toEntity())
    }

    override suspend fun countUser(): Int {
        return localDataSource.countUser()
    }

    override suspend fun register(userModel: UserModel): Boolean {
        return localDataSource.register(userModel.toEntity())
    }

    override suspend fun deleteUser(userModel: UserModel): Boolean {
        return localDataSource.deleteUser(userModel.toEntity())
    }
}