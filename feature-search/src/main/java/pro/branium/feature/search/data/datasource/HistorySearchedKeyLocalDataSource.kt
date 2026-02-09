package pro.branium.feature.search.data.datasource

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.user.UserSearchedKeyCrossRefEntity
import pro.branium.feature.search.data.entity.HistorySearchedKeyEntity

interface HistorySearchedKeyLocalDataSource {
    suspend fun insertKey(key: HistorySearchedKeyEntity): Long

    suspend fun insertKeyCrossRef(crossRef: UserSearchedKeyCrossRefEntity)

    fun getHistorySearchedKeyIdsForUser(userId: Int): Flow<List<HistorySearchedKeyEntity>>

    suspend fun removeKey(key: String)

    suspend fun clearAllKeys()
}