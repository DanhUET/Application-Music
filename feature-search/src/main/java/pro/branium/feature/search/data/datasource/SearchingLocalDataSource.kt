package pro.branium.feature.search.data.datasource

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity

interface SearchingLocalDataSource {
    suspend fun search(query: String): List<SongEntity>

    fun getHistorySearchedSongsForUserByIds(songIds: List<String>): Flow<List<SongEntity>>
}