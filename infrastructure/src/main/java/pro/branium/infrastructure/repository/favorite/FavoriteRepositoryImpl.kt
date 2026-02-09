package pro.branium.infrastructure.repository.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.core_domain.manager.UserManager
import pro.branium.core_domain.repository.FavoriteRepository
import pro.branium.core_model.SongModel
import pro.branium.feature_favorite.data.datasource.FavoriteLocalDataSource
import pro.branium.infrastructure.mapper.local.toModel
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dataSource: FavoriteLocalDataSource,
    private val userManager: UserManager
) : FavoriteRepository {
    override suspend fun addFavorite(songId: String) {
        val userId = userManager.getCurrentUserId() // "local" nếu chưa đăng nhập
        dataSource.addFavorite(songId, userId)
    }

    override fun toggleFavorite(songId: String): Boolean {
        // todo
        return false
    }

    override suspend fun deleteFavorite(songId: String) {
        val userId = userManager.getCurrentUserId() // "local" nếu chưa đăng nhập
        dataSource.removeFavorite(songId, userId)
    }

    override fun getFavorites(): Flow<List<SongModel>> {
        val userId = userManager.getCurrentUserId()
        return dataSource.getFavorites(userId).map { entities: List<SongEntity> ->
            entities.map { entity -> entity.toModel() }
        }
    }

    override fun isFavorite(songId: String): Flow<Boolean> {
        val userId = userManager.getCurrentUserId()
        return dataSource.isFavorite(songId, userId)
    }

    override fun getLimitedFavoriteSong(limit: Int): Flow<List<SongModel>> {
        val userId = userManager.getCurrentUserId()
        return dataSource.getLimitedFavorites(userId, limit).map { entities: List<SongEntity> ->
            entities.map { entity -> entity.toModel() }.take(limit)
        }
    }
}