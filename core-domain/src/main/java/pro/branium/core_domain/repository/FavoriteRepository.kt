package pro.branium.core_domain.repository

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.SongModel

interface FavoriteRepository {
    suspend fun addFavorite(songId: String)

    fun toggleFavorite(songId: String): Boolean

    suspend fun deleteFavorite(songId: String)

    fun getFavorites(): Flow<List<SongModel>>

    fun isFavorite(songId: String): Flow<Boolean>

    fun getLimitedFavoriteSong(limit: Int): Flow<List<SongModel>>
}