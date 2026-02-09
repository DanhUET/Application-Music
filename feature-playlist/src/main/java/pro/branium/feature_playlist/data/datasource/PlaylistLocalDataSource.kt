package pro.branium.feature_playlist.data.datasource

import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.playlist.PlaylistEntity
import pro.branium.core_database.entity.playlist.PlaylistWithCountEntity
import pro.branium.core_database.entity.playlist.PlaylistWithSongs
import pro.branium.core_database.entity.song.SongEntity

interface PlaylistLocalDataSource {
    fun getAllPlaylists(): Flow<List<PlaylistWithCountEntity>>

    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    fun getLimitedPlaylists(limit: Int): Flow<List<PlaylistEntity>>

    fun getLimitedPlaylistsWithSongs(limit: Int): Flow<List<PlaylistWithSongs>>

    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>

    suspend fun getSongsForPlaylist(playlistId: Long): List<SongEntity>

    fun getPlaylistsWithSongsByPlaylistId(playlistId: Long): Flow<PlaylistWithSongs>

    suspend fun getMaxPlaylistId(): Long?

    suspend fun isSongInPlaylist(playlistId: Long, songId: String): Boolean

    suspend fun findPlaylistByName(name: String): PlaylistEntity?

    fun getPlaylistsWithCount(limit: Int): Flow<List<PlaylistWithCountEntity>>

    suspend fun createPlaylist(playlistModel: PlaylistEntity)

    suspend fun insertPlaylistSongCrossRef(playlistId: Long, songId: String): Long

    suspend fun deletePlaylist(playlistModel: PlaylistEntity)

    suspend fun renamePlaylist(playlistModel: PlaylistEntity)
}