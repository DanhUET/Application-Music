package pro.branium.infrastructure.repository.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pro.branium.core_model.SongModel
import pro.branium.feature_favorite.data.datasource.FavoriteLocalDataSource
import pro.branium.feature_user.data.datasource.UserFavoriteSongLocalDataSource
import pro.branium.feature_user.domain.repository.UserFavoriteSongRepository
import javax.inject.Inject

class UserFavoriteSongRepositoryImpl @Inject constructor(
    private val localDataSource: UserFavoriteSongLocalDataSource,
    private val songLocalDataSource: FavoriteLocalDataSource
) : UserFavoriteSongRepository {
    override suspend fun isFavoriteSong(songId: String, userId: Int): Boolean {
        return localDataSource.isFavoriteSong(songId, userId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLimitedSongIdsForUser(userId: Int, limit: Int): Flow<List<SongModel>> {
        return flowOf(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavoriteSongsForUser(userId: Int): Flow<List<SongModel>> {
        return flowOf(emptyList())
    }

//    override suspend fun insertCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity) {
//        localDataSource.insertCrossRef(*crossRef)
//    }
//
//    override suspend fun deleteCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity) {
//        localDataSource.deleteCrossRef(*crossRef)
//    }

    override suspend fun clearAll() {
        localDataSource.clearAll()
    }
}