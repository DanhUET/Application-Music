package pro.branium.feature_favorite.data.datasource

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity

interface FavoriteLocalDataSource {
    suspend fun addFavorite(songId: String, userId: Int = 0)
    suspend fun removeFavorite(songId: String, userId: Int = 0)
    fun getFavorites(userId: Int = 0): Flow<List<SongEntity>>
    fun isFavorite(songId: String, userId: Int = 0): Flow<Boolean>
    fun getLimitedFavorites(userId: Int = 0, limit: Int): Flow<List<SongEntity>>
}