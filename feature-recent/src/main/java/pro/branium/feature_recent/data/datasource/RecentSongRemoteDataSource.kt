package pro.branium.feature_recent.data.datasource

import pro.branium.core_network.dto.song.SongDto

interface RecentSongRemoteDataSource {
    suspend fun getRecentSongs(userId: Int, limit: Int, offset: Int): List<SongDto>

    suspend fun setRecentSong(userId: Int, songId: String, createdAt: Long)
}