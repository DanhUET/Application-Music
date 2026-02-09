package pro.branium.infrastructure.source.searching

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.feature.search.data.dao.SearchingDao
import pro.branium.feature.search.data.datasource.SearchingLocalDataSource
import javax.inject.Inject

class SearchingLocalDataSourceImpl @Inject constructor(
    private val searchingDao: SearchingDao
) : SearchingLocalDataSource {
    override suspend fun search(query: String): List<SongEntity> {
        return searchingDao.search(query)
    }

    override fun getHistorySearchedSongsForUserByIds(songIds: List<String>): Flow<List<SongEntity>> {
        return searchingDao.getHistorySearchedSongsForUserByIds(songIds)
    }
}