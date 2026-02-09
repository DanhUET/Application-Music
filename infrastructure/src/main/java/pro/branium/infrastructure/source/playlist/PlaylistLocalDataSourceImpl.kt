package pro.branium.infrastructure.source.playlist

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.dao.playlist.PlaylistDao
import pro.branium.core_database.dao.playlist.PlaylistSongCrossRefDao
import pro.branium.core_database.dao.song.SongDao
import pro.branium.core_database.entity.playlist.PlaylistEntity
import pro.branium.core_database.entity.playlist.PlaylistSongCrossRefEntity
import pro.branium.core_database.entity.playlist.PlaylistWithCountEntity
import pro.branium.core_database.entity.playlist.PlaylistWithSongs
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.feature_playlist.data.datasource.PlaylistLocalDataSource
import javax.inject.Inject

class PlaylistLocalDataSourceImpl @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val playlistSongCrossRefDao: PlaylistSongCrossRefDao,
    private val songDao: SongDao
) : PlaylistLocalDataSource {
    override fun getAllPlaylists(): Flow<List<PlaylistWithCountEntity>> {
        return playlistDao.getAllPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Long): PlaylistEntity? {
        return playlistDao.getPlaylistById(playlistId)
    }

    override fun getLimitedPlaylists(limit: Int): Flow<List<PlaylistEntity>> {
        return playlistDao.getLimitedPlaylists(limit)
    }

    override fun getLimitedPlaylistsWithSongs(limit: Int): Flow<List<PlaylistWithSongs>> {
        return playlistDao.getLimitedPlaylistsWithSongs(limit)
    }

    override fun getAllPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>> {
        return playlistDao.getAllPlaylistsWithSongs()
    }

    override suspend fun getSongsForPlaylist(playlistId: Long): List<SongEntity> {
        val songIds = playlistSongCrossRefDao
            .getSongIdsForPlaylist(playlistId)
        return songDao.getSongsForPlaylistByIds(songIds)
    }

    override fun getPlaylistsWithSongsByPlaylistId(playlistId: Long): Flow<PlaylistWithSongs> {
        return playlistDao.getPlaylistsWithSongsByPlaylistId(playlistId)
    }

    override suspend fun getMaxPlaylistId(): Long? = playlistDao.getMaxPlaylistId()

    override suspend fun isSongInPlaylist(playlistId: Long, songId: String): Boolean {
        return playlistSongCrossRefDao.isSongInPlaylist(playlistId, songId)
    }

    override suspend fun findPlaylistByName(name: String): PlaylistEntity? {
        return playlistDao.findPlaylistByName(name)
    }

    override fun getPlaylistsWithCount(limit: Int): Flow<List<PlaylistWithCountEntity>> {
        return playlistDao.getPlaylistWithCount(limit)
    }

    override suspend fun createPlaylist(playlistModel: PlaylistEntity) {
        playlistDao.insert(playlistModel)
    }

    override suspend fun insertPlaylistSongCrossRef(playlistId: Long, songId: String): Long {
        return try {
            val crossRef = PlaylistSongCrossRefEntity(playlistId, songId)
            playlistSongCrossRefDao.insertCrossRef(crossRef)
        } catch (_: SQLiteConstraintException) {
            -1L
        }
    }

    override suspend fun deletePlaylist(playlistModel: PlaylistEntity) {
        playlistDao.delete(playlistModel)
    }

    override suspend fun renamePlaylist(playlistModel: PlaylistEntity) {
        playlistDao.renamePlaylist(playlistModel.playlistId, playlistModel.name)
    }
}