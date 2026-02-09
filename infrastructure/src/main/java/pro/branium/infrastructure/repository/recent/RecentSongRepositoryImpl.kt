package pro.branium.infrastructure.repository.recent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.branium.core_domain.manager.UserManager
import pro.branium.core_domain.repository.RecentSongRepository
import pro.branium.core_model.SongModel
import pro.branium.feature_recent.data.datasource.RecentSongLocalDataSource
import pro.branium.feature_recent.data.datasource.RecentSongRemoteDataSource
import pro.branium.infrastructure.mapper.local.songEntitiesToModels
import pro.branium.infrastructure.mapper.local.toModel
import javax.inject.Inject

class RecentSongRepositoryImpl @Inject constructor(
    private val localDataSource: RecentSongLocalDataSource,
    private val remoteDataSource: RecentSongRemoteDataSource,
    private val userManager: UserManager
) : RecentSongRepository {
    override fun getLimitedRecentSongs(limit: Int): Flow<List<SongModel>> {
        val userId = userManager.getCurrentUserId()
        return localDataSource
            .getLimitedRecentSongs(userId, limit)
            .map { songs ->
                songs.songEntitiesToModels()
            }
    }

    override fun getMoreRecentSongs(): Flow<List<SongModel>> {
        val userId = userManager.getCurrentUserId()
        return localDataSource
            .getRecentSongs(userId)
            .map { songs -> songs.songEntitiesToModels() }
    }

    override fun getMostListenedSongIdsForUser(limit: Int): Flow<List<SongModel>> {
        val userId = userManager.getCurrentUserId()
        return localDataSource
            .getMostListenedSongIdsForUser(userId, limit)
            .map { songs ->
                songs.songEntitiesToModels()
            }
    }

    override suspend fun setRecentSong(songId: String) {
        val userId = userManager.getCurrentUserId()
        val createdAt = System.currentTimeMillis()
        remoteDataSource.setRecentSong(userId, songId, createdAt)
        localDataSource.setRecentSong(userId, songId)
    }

    override fun getMostRecentSong(): Flow<SongModel?> {
        val userId = userManager.getCurrentUserId()
        return localDataSource
            .getMostRecentSong(userId)
            .map { song ->
                song?.toModel()
            }

    }
}