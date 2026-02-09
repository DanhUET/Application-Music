package pro.branium.feature_recent.data.datasource

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity

interface RecentSongLocalDataSource {
    fun getLimitedRecentSongs(userId: Int, limit: Int): Flow<List<SongEntity>>

    fun getRecentSongs(userId: Int): Flow<List<SongEntity>>

    fun getMostListenedSongIdsForUser(userId: Int, limit: Int): Flow<List<SongEntity>>

    suspend fun setRecentSong(userId: Int, songId: String)

    fun getMostRecentSong(userId: Int): Flow<SongEntity?>
}