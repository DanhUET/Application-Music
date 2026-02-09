package pro.branium.core_domain.playlist.repository

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.PlaylistModel
import pro.branium.core_model.PlaylistSummary
import pro.branium.core_model.SongModel

interface PlaylistRepository {
    fun getAllPlaylists(): Flow<List<PlaylistSummary>>

    suspend fun getPlaylistById(playlistId: Long): PlaylistModel?

    fun getLimitedPlaylists(limit: Int): Flow<List<PlaylistModel>>

    fun getLimitedPlaylistsWithSongs(limit: Int): Flow<List<PlaylistModel>>

    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistModel>>

    suspend fun getSongsForPlaylist(playlistId: Long): List<SongModel>

    fun getPlaylistsWithSongsByPlaylistId(playlistId: Long): Flow<PlaylistModel>

    suspend fun getMaxPlaylistId(): Long?

    suspend fun isSongInPlaylist(playlistId: Long, songId: String): Boolean

    suspend fun findPlaylistByName(name: String): PlaylistModel?

    fun getPlaylistsWithCount(limit: Int): Flow<List<PlaylistSummary>>

    suspend fun createPlaylist(playlistModel: PlaylistModel)

    suspend fun insertPlaylistSongCrossRef(playlistId: Long, songId: String): Long

    suspend fun deletePlaylist(playlistModel: PlaylistModel)

    suspend fun renamePlaylist(playlistModel: PlaylistModel)
}