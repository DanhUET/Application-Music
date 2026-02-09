package pro.branium.infrastructure.source.searching

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import pro.branium.core_database.dao.user.UserSearchedKeyCrossRefDao
import pro.branium.core_database.entity.user.UserSearchedKeyCrossRefEntity
import pro.branium.feature.search.data.dao.HistorySearchedKeyDao
import pro.branium.feature.search.data.datasource.HistorySearchedKeyLocalDataSource
import pro.branium.feature.search.data.entity.HistorySearchedKeyEntity
import javax.inject.Inject

class HistorySearchedKeyLocalDataSourceImpl @Inject constructor(
    private val dao: HistorySearchedKeyDao,
    private val crossRefDao: UserSearchedKeyCrossRefDao
) : HistorySearchedKeyLocalDataSource {
    override suspend fun insertKey(key: HistorySearchedKeyEntity): Long {
        return dao.insertKey(key)
    }

    override suspend fun insertKeyCrossRef(crossRef: UserSearchedKeyCrossRefEntity) {
        crossRefDao.insertCrossRef(crossRef)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getHistorySearchedKeyIdsForUser(userId: Int): Flow<List<HistorySearchedKeyEntity>> {
        return crossRefDao
            .getSearchedSongIdsForUser(userId)
            .flatMapLatest { keyIds -> dao.getHistorySearchedKeysForUserByIds(keyIds) }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun removeKey(key: String) {
        val keyId = dao.removeKey(key)
        crossRefDao.remove(keyId)
    }

    override suspend fun clearAllKeys() {
        crossRefDao.clearAll()
        dao.clearAllKeys()
    }
}