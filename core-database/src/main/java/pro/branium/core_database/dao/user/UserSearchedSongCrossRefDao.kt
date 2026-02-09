package pro.branium.core_database.dao.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.user.UserSearchedSongCrossRefEntity

@Dao
interface UserSearchedSongCrossRefDao {
    @Query(
        "SELECT song_id FROM user_searched_song_cross_ref " +
                "WHERE user_id = :userId " +
                "ORDER BY created_at DESC " +
                "LIMIT 100"
    )
    fun getSearchedSongIdsForUser(userId: Int): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCrossRef(vararg crossRef: UserSearchedSongCrossRefEntity)

    @Delete
    suspend fun deleteCrossRef(vararg crossRef: UserSearchedSongCrossRefEntity)


    @Query("DELETE FROM user_searched_song_cross_ref")
    suspend fun clearAll()
}