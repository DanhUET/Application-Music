package pro.branium.infrastructure.repository.searching

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.branium.core_database.entity.user.UserSearchedKeyCrossRefEntity
import pro.branium.core_domain.manager.UserManager
import pro.branium.feature.search.data.datasource.HistorySearchedKeyLocalDataSource
import pro.branium.feature.search.data.entity.HistorySearchedKeyEntity
import pro.branium.feature.search.domain.model.HistorySearchedKeyModel
import pro.branium.feature.search.domain.repository.HistorySearchedKeyRepository
import pro.branium.infrastructure.mapper.local.toModels
import javax.inject.Inject

class HistorySearchedKeyRepositoryImpl @Inject constructor(
    private val localDataSource: HistorySearchedKeyLocalDataSource,
    private val userManager: UserManager
) : HistorySearchedKeyRepository {
    override suspend fun insertKey(key: String): Long {
        val entity = HistorySearchedKeyEntity(key = key)
        return localDataSource.insertKey(entity)
    }

    override suspend fun insertKeyCrossRef(keyId: Long) {
        val userId = userManager.getCurrentUserId()
        val crossRef = UserSearchedKeyCrossRefEntity(userId, keyId)
        localDataSource.insertKeyCrossRef(crossRef)
    }

    override fun getHistorySearchedKeysForUser(
    ): Flow<List<HistorySearchedKeyModel>> {
        val userId = userManager.getCurrentUserId()
        val models = localDataSource
            .getHistorySearchedKeyIdsForUser(userId)
            .map { it.toModels() }
        return models
    }

    override suspend fun removeKey(key: String) {
        localDataSource.removeKey(key)
    }

    override suspend fun clearAllKeys() {
        localDataSource.clearAllKeys()
    }
}