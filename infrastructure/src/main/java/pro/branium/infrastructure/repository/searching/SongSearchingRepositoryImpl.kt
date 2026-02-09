package pro.branium.infrastructure.repository.searching

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import pro.branium.core_database.entity.user.UserSearchedSongCrossRefEntity
import pro.branium.core_domain.manager.UserManager
import pro.branium.core_model.SongModel
import pro.branium.feature.search.data.datasource.HistorySearchedSongLocalDataSource
import pro.branium.feature.search.data.datasource.SearchingLocalDataSource
import pro.branium.feature.search.data.datasource.SearchingRemoteDataSource
import pro.branium.feature.search.domain.repository.SongSearchingRepository
import pro.branium.infrastructure.mapper.local.songEntitiesToModels
import pro.branium.infrastructure.mapper.remote.toModels
import javax.inject.Inject

class SongSearchingRepositoryImpl @Inject constructor(
    private val searchingRemoteDataSource: SearchingRemoteDataSource,
    private val searchingLocalDataSource: SearchingLocalDataSource,
    private val userSearchedSongLocalDataSource: HistorySearchedSongLocalDataSource,
    private val userManager: UserManager
) : SongSearchingRepository {
    override suspend fun search(query: String): List<SongModel> {
        val localSongs = searchingLocalDataSource.search(query).songEntitiesToModels()
        val remoteSongs = searchingRemoteDataSource.search(query).toModels()
        val mergedList: List<SongModel> = localSongs + remoteSongs
        val uniqueSongs = mergedList.distinctBy { it.id }
        val sortedSongs = uniqueSongs.sortedBy { it.title }
        return sortedSongs
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getHistorySearchedSongsForUser(): Flow<List<SongModel>> {
        val userId = userManager.getCurrentUserId()
        return userSearchedSongLocalDataSource
            .getSearchedSongIdsForUser(userId)
            .flatMapLatest { ids ->
                searchingLocalDataSource.getHistorySearchedSongsForUserByIds(ids)
            }
            .flowOn(Dispatchers.IO)
            .map { result -> result.songEntitiesToModels() }
    }

    override suspend fun insertCrossRef(songId: String) {
        val userId = userManager.getCurrentUserId()
        val crossRef = UserSearchedSongCrossRefEntity(userId = userId, songId = songId)
        userSearchedSongLocalDataSource.insertCrossRef(crossRef)
    }

    override suspend fun deleteCrossRef(songId: String) {
        val userId = userManager.getCurrentUserId()
        val crossRef = UserSearchedSongCrossRefEntity(userId = userId, songId = songId)
        userSearchedSongLocalDataSource.deleteCrossRef(crossRef)
    }

    override suspend fun clearAllHistorySongs() {
        userSearchedSongLocalDataSource.clearAll()
    }
}