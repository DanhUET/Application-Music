package pro.branium.feature.search.data.datasource

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.user.UserSearchedSongCrossRefEntity

interface HistorySearchedSongLocalDataSource {
    fun getSearchedSongIdsForUser(userId: Int): Flow<List<String>>

    suspend fun insertCrossRef(vararg crossRef: UserSearchedSongCrossRefEntity)

    suspend fun deleteCrossRef(vararg crossRef: UserSearchedSongCrossRefEntity)

    suspend fun clearAll()
}