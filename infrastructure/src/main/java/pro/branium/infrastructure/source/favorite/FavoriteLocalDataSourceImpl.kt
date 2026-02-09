package pro.branium.infrastructure.source.favorite

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.feature_favorite.data.dao.FavoriteSongDao
import pro.branium.feature_favorite.data.datasource.FavoriteLocalDataSource
import pro.branium.feature_favorite.data.entity.FavoriteEntity
import javax.inject.Inject

class FavoriteLocalDataSourceImpl @Inject constructor(
    private val favoriteDao: FavoriteSongDao
) : FavoriteLocalDataSource {
    override suspend fun addFavorite(songId: String, userId: Int) {
        favoriteDao.insertFavorite(FavoriteEntity(userId, songId))
    }

    override suspend fun removeFavorite(songId: String, userId: Int) {
        favoriteDao.deleteFavorite(FavoriteEntity(userId, songId))
    }

    override fun getFavorites(userId: Int): Flow<List<SongEntity>> {
        return favoriteDao.getFavoriteSongs(userId)
    }

    override fun isFavorite(songId: String, userId: Int): Flow<Boolean> {
        return favoriteDao.isFavorite(userId, songId)
    }

    override fun getLimitedFavorites(userId: Int, limit: Int): Flow<List<SongEntity>> {
        return favoriteDao.getLimitedFavoriteSongs(userId, limit)
    }
}