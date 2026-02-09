package pro.branium.feature_user.domain.repository

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.SongModel

interface UserFavoriteSongRepository {
    suspend fun isFavoriteSong(songId: String, userId: Int): Boolean

    fun getLimitedSongIdsForUser(userId: Int, limit: Int): Flow<List<SongModel>>

    fun getFavoriteSongsForUser(userId: Int): Flow<List<SongModel>>

//    suspend fun insertCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity)

//    suspend fun deleteCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity)

    suspend fun clearAll()
}