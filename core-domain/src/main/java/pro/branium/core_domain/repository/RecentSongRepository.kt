package pro.branium.core_domain.repository

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.SongModel

interface RecentSongRepository {
    fun getLimitedRecentSongs(limit: Int): Flow<List<SongModel>>

    fun getMoreRecentSongs(): Flow<List<SongModel>>

    fun getMostListenedSongIdsForUser(limit: Int): Flow<List<SongModel>>

    suspend fun setRecentSong(songId: String)

    fun getMostRecentSong(): Flow<SongModel?>
}