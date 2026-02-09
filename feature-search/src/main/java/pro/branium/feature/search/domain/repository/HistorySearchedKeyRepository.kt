package pro.branium.feature.search.domain.repository

import kotlinx.coroutines.flow.Flow
import pro.branium.feature.search.domain.model.HistorySearchedKeyModel

interface HistorySearchedKeyRepository {
    suspend fun insertKey(key: String): Long

    suspend fun insertKeyCrossRef(keyId: Long)

    fun getHistorySearchedKeysForUser(): Flow<List<HistorySearchedKeyModel>>

    suspend fun removeKey(key: String)

    suspend fun clearAllKeys()
}