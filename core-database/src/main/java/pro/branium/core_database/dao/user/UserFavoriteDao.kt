package pro.branium.core_database.dao.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.user.UserFavoriteSongCrossRefEntity

@Dao
interface UserFavoriteDao {
    @Query(
        "SELECT * FROM user_favorite_song_cross_ref " +
                "WHERE song_id = :songId AND user_id = :userId"
    )
    suspend fun isFavoriteSong(songId: String, userId: Int): UserFavoriteSongCrossRefEntity?

    @Query(
        "SELECT song_id FROM user_favorite_song_cross_ref " +
                "WHERE user_id = :userId " +
                "ORDER BY created_at ASC " +
                "LIMIT :limit"
    )
    fun getLimitedSongIdsForUser(userId: Int, limit: Int): Flow<List<String>>

    @Query(
        "SELECT song_id FROM user_favorite_song_cross_ref " +
                "WHERE user_id = :userId " +
                "ORDER BY created_at ASC "
    )
    fun getSongIdsForUser(userId: Int): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity)

    @Delete
    suspend fun deleteCrossRef(vararg crossRef: UserFavoriteSongCrossRefEntity)

    @Query("DELETE FROM user_favorite_song_cross_ref")
    suspend fun clearAll()
}