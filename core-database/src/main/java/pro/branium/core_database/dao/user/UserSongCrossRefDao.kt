package pro.branium.core_database.dao.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.core_database.entity.user.UserSongCrossRefEntity

@Dao
interface UserSongCrossRefDao {
    @Query(
        """
            SELECT s.* FROM songs AS s
        INNER JOIN user_song_cross_ref AS uscr 
            ON s.song_id = uscr.song_id
        WHERE uscr.user_id = :userId
        ORDER BY uscr.updated_at DESC
        LIMIT 1
        """
    )
    fun getMostRecentSong(userId: Int): Flow<SongEntity?>

    @Query("SELECT * FROM user_song_cross_ref " +
            "WHERE song_id = :songId AND user_id = :userId")
    suspend fun getBySongId(songId: String, userId: Int): UserSongCrossRefEntity?

    @Query(
        "SELECT song_id FROM user_song_cross_ref " +
                "WHERE user_id = :userId " +
                "ORDER BY updated_at DESC " +
                "LIMIT :limit"
    )
    fun getLimitedSongIdsForUser(userId: Int, limit: Int): Flow<List<String>>

    @Query(
        "SELECT song_id FROM user_song_cross_ref " +
                "WHERE user_id = :userId " +
                "ORDER BY updated_at DESC " +
                "LIMIT 100"
    )
    fun getSongIdsForUser(userId: Int): Flow<List<String>>

    @Query(
        "SELECT song_id FROM user_song_cross_ref " +
                "WHERE user_id = :userId " +
                "ORDER BY replay DESC " +
                "LIMIT :limit"
    )
    fun getMostListenedSongIdsForUser(userId: Int, limit: Int): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertCrossRef(vararg userSongCrossRefEntity: UserSongCrossRefEntity)

    @Query(
        "UPDATE user_song_cross_ref SET replay = replay + 1, updated_at = :updatedAt " +
                "WHERE song_id = :songId AND user_id = :userId"
    )
    suspend fun updateCrossRef(songId: String, userId: Int, updatedAt: Long)

    @Delete
    suspend fun deleteCrossRef(vararg userSongCrossRefEntity: UserSongCrossRefEntity)


    @Query("DELETE FROM user_song_cross_ref")
    suspend fun clearAll()
}