package pro.branium.infrastructure.source.searching

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.dao.user.UserSearchedSongCrossRefDao
import pro.branium.core_database.entity.user.UserSearchedSongCrossRefEntity
import pro.branium.feature.search.data.datasource.HistorySearchedSongLocalDataSource
import javax.inject.Inject

class HistorySearchedSongLocalDataSourceImpl @Inject constructor(
    private val crossRefDao: UserSearchedSongCrossRefDao
) : HistorySearchedSongLocalDataSource {
    override fun getSearchedSongIdsForUser(userId: Int): Flow<List<String>> {
        return crossRefDao.getSearchedSongIdsForUser(userId)
    }

    override suspend fun insertCrossRef(vararg crossRef: UserSearchedSongCrossRefEntity) {
        crossRefDao.insertCrossRef(*crossRef)
    }

    override suspend fun deleteCrossRef(vararg crossRef: UserSearchedSongCrossRefEntity) {
        crossRefDao.deleteCrossRef(*crossRef)
    }

    override suspend fun clearAll() {
        crossRefDao.clearAll()
    }
}