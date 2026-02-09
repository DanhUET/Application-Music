package pro.branium.infrastructure.source.recent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import pro.branium.core_database.dao.song.SongDao
import pro.branium.core_database.dao.user.UserSongCrossRefDao
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.core_database.entity.user.UserSongCrossRefEntity
import pro.branium.feature_recent.data.datasource.RecentSongLocalDataSource
import javax.inject.Inject

class RecentSongLocalDataSourceImpl @Inject constructor(
    private val recentSongDao: UserSongCrossRefDao,
    private val songDao: SongDao
) : RecentSongLocalDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLimitedRecentSongs(userId: Int, limit: Int): Flow<List<SongEntity>> {
        return recentSongDao.getLimitedSongIdsForUser(userId, limit)
            .flatMapLatest { songIds ->
                songDao.getRecentSongsByIds(songIds)
            }.flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getRecentSongs(userId: Int): Flow<List<SongEntity>> {
        return recentSongDao.getSongIdsForUser(userId)
            .flatMapLatest { songIds ->
                songDao.getRecentSongsByIds(songIds)
            }.flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMostListenedSongIdsForUser(userId: Int, limit: Int): Flow<List<SongEntity>> {
        return recentSongDao.getMostListenedSongIdsForUser(userId, limit)
            .flatMapLatest { songIds ->
                songDao.getMostListenedSongsByIds(songIds)
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun setRecentSong(userId: Int, songId: String) {
        // if recent song is not exist -> create new else update
        val targetSong = recentSongDao.getBySongId(songId, userId)
        val crossRef = UserSongCrossRefEntity(songId, userId, System.currentTimeMillis())
        if (targetSong == null) {
            recentSongDao.insertCrossRef(crossRef)
        } else {
            recentSongDao.updateCrossRef(songId, userId, crossRef.updatedAt)
        }
    }

    override fun getMostRecentSong(userId: Int): Flow<SongEntity?> {
        return recentSongDao.getMostRecentSong(userId)
    }
}