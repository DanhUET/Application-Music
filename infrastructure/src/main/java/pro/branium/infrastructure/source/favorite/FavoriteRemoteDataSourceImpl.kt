package pro.branium.infrastructure.source.favorite

import kotlinx.coroutines.flow.Flow
import pro.branium.core_network.dto.song.SongDto
import pro.branium.feature_favorite.data.datasource.FavoriteRemoteDataSource
import javax.inject.Inject

class FavoriteRemoteDataSourceImpl @Inject constructor() : FavoriteRemoteDataSource {
    override suspend fun addFavorite(songId: String, userId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(songId: String, userId: Int) {
        TODO("Not yet implemented")
    }

    override fun getFavorites(userId: Int): Flow<List<SongDto>> {
        TODO("Not yet implemented")
    }

    override fun isFavorite(
        songId: String,
        userId: Int
    ): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLimitedFavorites(
        userId: Int,
        limit: Int
    ): Flow<List<SongDto>> {
        TODO("Not yet implemented")
    }

}