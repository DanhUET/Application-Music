package pro.branium.core_database.dao.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.playlist.PlaylistEntity
import pro.branium.core_database.entity.playlist.PlaylistWithCountEntity
import pro.branium.core_database.entity.playlist.PlaylistWithSongs

@Dao
interface PlaylistDao {
    @Query(
        "SELECT p.playlist_id, p.name, p.artwork, COUNT(ps.song_id) AS song_count" +
                "        FROM playlists p" +
                "        LEFT JOIN playlist_song_cross_ref ps ON p.playlist_id = ps.playlist_id" +
                "        GROUP BY p.playlist_id "
    )
    fun getAllPlaylists(): Flow<List<PlaylistWithCountEntity>>

    @Query(
        "SELECT playlist_id, name, artwork, created_at " +
                "FROM playlists WHERE playlist_id = :playlistId"
    )
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Query(
        "SELECT playlist_id, name, artwork, created_at " +
                "FROM playlists ORDER BY created_at DESC LIMIT :limit"
    )
    fun getLimitedPlaylists(limit: Int): Flow<List<PlaylistEntity>>

    @Transaction
    @Query(
        "SELECT playlist_id, name, artwork, created_at " +
                "FROM playlists ORDER BY created_at DESC LIMIT :limit"
    )
    fun getLimitedPlaylistsWithSongs(limit: Int): Flow<List<PlaylistWithSongs>>

    @Transaction
    @Query(
        "SELECT playlist_id, name, artwork, created_at " +
                "FROM playlists ORDER BY created_at DESC"
    )
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>

    @Transaction
    @Query(
        "SELECT playlist_id, name, artwork, created_at " +
                "FROM playlists WHERE playlist_id = :playlistId"
    )
    fun getPlaylistsWithSongsByPlaylistId(playlistId: Long): Flow<PlaylistWithSongs>

    @Query("SELECT MAX(playlist_id) FROM playlists")
    suspend fun getMaxPlaylistId(): Long?

    @Query("SELECT playlist_id, name, artwork, created_at FROM playlists WHERE name LIKE :name")
    suspend fun findPlaylistByName(name: String): PlaylistEntity?

    @Query(
        "SELECT p.playlist_id, p.name, p.artwork, COUNT(ps.song_id) AS song_count" +
                "        FROM playlists p" +
                "        LEFT JOIN playlist_song_cross_ref ps ON p.playlist_id = ps.playlist_id" +
                "        GROUP BY p.playlist_id " +
                "        LIMIT :limit"
    )
    fun getPlaylistWithCount(limit: Int): Flow<List<PlaylistWithCountEntity>>

    @Insert
    suspend fun insert(playlistModel: PlaylistEntity)

    @Delete
    suspend fun delete(playlistModel: PlaylistEntity)

    @Query("UPDATE playlists SET name = :newName WHERE playlist_id = :playlistId")
    suspend fun renamePlaylist(playlistId: Long, newName: String)
}