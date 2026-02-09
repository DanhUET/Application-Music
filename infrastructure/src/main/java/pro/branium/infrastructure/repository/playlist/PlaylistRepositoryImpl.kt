package pro.branium.infrastructure.repository.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.branium.core_domain.playlist.repository.PlaylistRepository
import pro.branium.core_model.PlaylistModel
import pro.branium.core_model.PlaylistSummary
import pro.branium.core_model.SongModel
import pro.branium.feature_playlist.data.datasource.PlaylistLocalDataSource
import pro.branium.feature_playlist.data.datasource.PlaylistRemoteDataSource
import pro.branium.infrastructure.mapper.local.songEntitiesToModels
import pro.branium.infrastructure.mapper.local.toEntity
import pro.branium.infrastructure.mapper.local.toModel
import pro.branium.infrastructure.mapper.local.toModels
import pro.branium.infrastructure.mapper.local.toPlaylistSummaries
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val localDataSource: PlaylistLocalDataSource,
    private val remoteDataSource: PlaylistRemoteDataSource
) : PlaylistRepository {
    override fun getAllPlaylists(): Flow<List<PlaylistSummary>> {
        return localDataSource
            .getAllPlaylists()
            .map { playlists -> playlists.toPlaylistSummaries() }
    }

    override suspend fun getPlaylistById(playlistId: Long): PlaylistModel? {
        return localDataSource.getPlaylistById(playlistId)?.toModel()
    }

    override fun getLimitedPlaylists(limit: Int): Flow<List<PlaylistModel>> {
        return localDataSource
            .getLimitedPlaylists(limit)
            .map { playlists ->
                playlists.toModels()
            }
    }

    override fun getLimitedPlaylistsWithSongs(limit: Int): Flow<List<PlaylistModel>> {
        val playlistWithSongs = localDataSource.getLimitedPlaylistsWithSongs(limit)
        return playlistWithSongs.map { playlists ->
            playlists.toModels()
        }
    }

    override fun getAllPlaylistsWithSongs(): Flow<List<PlaylistModel>> {
        val playlistWithSongs = localDataSource.getAllPlaylistsWithSongs()
        return playlistWithSongs.map { playlists ->
            playlists.toModels()
        }
    }

    override suspend fun getSongsForPlaylist(playlistId: Long): List<SongModel> {
        return localDataSource
            .getSongsForPlaylist(playlistId)
            .songEntitiesToModels()
    }

    override fun getPlaylistsWithSongsByPlaylistId(playlistId: Long): Flow<PlaylistModel> {
        val playlistWithSongs = localDataSource.getPlaylistsWithSongsByPlaylistId(playlistId)
        return playlistWithSongs.map { playlistWithSongs -> playlistWithSongs.toModel() }
    }

    override suspend fun getMaxPlaylistId(): Long? {
        return localDataSource.getMaxPlaylistId()
    }

    override suspend fun isSongInPlaylist(playlistId: Long, songId: String): Boolean {
        return localDataSource.isSongInPlaylist(playlistId, songId)
    }

    override suspend fun findPlaylistByName(name: String): PlaylistModel? {
        return localDataSource.findPlaylistByName(name)?.toModel()
    }

    override fun getPlaylistsWithCount(limit: Int): Flow<List<PlaylistSummary>> {
        val playlistWithCount = localDataSource.getPlaylistsWithCount(limit)
        return playlistWithCount.map { playlists -> playlists.toPlaylistSummaries() }
    }

    override suspend fun createPlaylist(playlistModel: PlaylistModel) {
        localDataSource.createPlaylist(playlistModel.toEntity())
    }

    override suspend fun insertPlaylistSongCrossRef(playlistId: Long, songId: String): Long {
        return localDataSource.insertPlaylistSongCrossRef(playlistId, songId)
    }

    override suspend fun deletePlaylist(playlistModel: PlaylistModel) {
        localDataSource.deletePlaylist(playlistModel.toEntity())
    }

    override suspend fun renamePlaylist(playlistModel: PlaylistModel) {
        localDataSource.renamePlaylist(playlistModel.toEntity())
    }
}