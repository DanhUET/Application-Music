package pro.branium.core_domain.manager

import kotlinx.coroutines.flow.StateFlow

interface FavoriteManager {
    val isFavorite: StateFlow<Boolean>
    suspend fun toggleFavorite()
    fun setFavoriteSongId(newSongId: String?)
}