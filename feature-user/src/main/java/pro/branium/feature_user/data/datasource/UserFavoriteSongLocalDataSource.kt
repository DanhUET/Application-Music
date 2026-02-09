package pro.branium.feature_user.data.datasource

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.user.UserFavoriteSongCrossRefEntity

interface UserFavoriteSongLocalDataSource {
    suspend fun isFavoriteSong(songId: String, userId: Int): Boolean

    fun getLimitedSongIdsForUser(userId: Int, limit: Int): Flow<List<String>>

    fun getSongIdsForUser(userId: Int): Flow<List<String>>

    suspend fun insertCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity)

    suspend fun deleteCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity)

    suspend fun clearAll()
}