package pro.branium.feature.search.domain.repository

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.SongModel

interface SongSearchingRepository {
    suspend fun search(query: String): List<SongModel>

    fun getHistorySearchedSongsForUser(): Flow<List<SongModel>>

    suspend fun insertCrossRef(songId: String)

    suspend fun deleteCrossRef(songId: String)

    suspend fun clearAllHistorySongs()
}