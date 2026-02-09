package pro.branium.infrastructure.source.user

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.dao.user.UserFavoriteDao
import pro.branium.core_database.entity.user.UserFavoriteSongCrossRefEntity
import pro.branium.feature_user.data.datasource.UserFavoriteSongLocalDataSource
import javax.inject.Inject

class UserFavoriteSongLocalDataSourceImpl @Inject constructor(
    private val userFavoriteDao: UserFavoriteDao
) : UserFavoriteSongLocalDataSource {
    override suspend fun isFavoriteSong(songId: String, userId: Int): Boolean {
        return userFavoriteDao.isFavoriteSong(songId, userId) != null
    }

    override fun getLimitedSongIdsForUser(userId: Int, limit: Int): Flow<List<String>> {
        return userFavoriteDao.getLimitedSongIdsForUser(userId, limit)
    }

    override fun getSongIdsForUser(userId: Int): Flow<List<String>> {
        return userFavoriteDao.getSongIdsForUser(userId)
    }

    override suspend fun insertCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity) {
        userFavoriteDao.insertCrossRef(*crossRef)
    }

    override suspend fun deleteCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity) {
        userFavoriteDao.deleteCrossRef(*crossRef)
    }

    override suspend fun clearAll() {
        userFavoriteDao.clearAll()
    }
}