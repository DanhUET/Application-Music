package pro.branium.infrastructure.source.user

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.dao.user.UserDao
import pro.branium.core_database.entity.user.UserEntity
import pro.branium.feature_user.data.datasource.UserLocalDataSource
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : UserLocalDataSource {
    override fun getUserInfo(userId: Int): Flow<UserEntity?> {
        return userDao.getUser(userId)
    }

    override suspend fun login(userModel: UserEntity): UserEntity? {
        return userDao.login(userModel.email,
            userModel.phoneNumber.toString(), userModel.password ?: "")
    }

    override suspend fun update(userModel: UserEntity): Boolean {
        return userDao.update(userModel) > 0
    }

    override suspend fun countUser(): Int {
        return userDao.countUser()
    }

    override suspend fun register(userModel: UserEntity): Boolean {
        return userDao.insert(userModel) > 0
    }

    override suspend fun deleteUser(userModel: UserEntity): Boolean {
        return userDao.delete(userModel) > 0
    }
}