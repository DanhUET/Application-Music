package pro.branium.core_database.dao.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.branium.core_database.entity.playlist.PlaylistSongCrossRefEntity

@Dao
interface PlaylistSongCrossRefDao {
    @Query(
        "SELECT song_id FROM playlist_song_cross_ref " +
                "WHERE playlist_id = :playlistId ORDER BY created_at ASC"
    )
    suspend fun getSongIdsForPlaylist(playlistId: Long): List<String>

    @Query("SELECT EXISTS (SELECT 1 FROM playlist_song_cross_ref " +
            "WHERE playlist_id = :playlistId AND song_id = :songId)")
    suspend fun isSongInPlaylist(playlistId: Long, songId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCrossRef(crossRef: PlaylistSongCrossRefEntity): Long

    @Delete
    suspend fun deleteCrossRef(vararg playlistSongCrossRefEntity: PlaylistSongCrossRefEntity)

    @Query("DELETE FROM playlist_song_cross_ref")
    suspend fun clearAll()
}