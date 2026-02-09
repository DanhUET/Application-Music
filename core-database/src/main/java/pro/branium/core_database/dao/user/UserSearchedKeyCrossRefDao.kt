package pro.branium.core_database.dao.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.user.UserSearchedKeyCrossRefEntity

@Dao
interface UserSearchedKeyCrossRefDao {
    @Query(
        "SELECT key_id FROM user_searched_key_cross_ref " +
                "WHERE user_id = :userId " +
                "ORDER BY created_at DESC " +
                "LIMIT 100"
    )
    fun getSearchedSongIdsForUser(userId: Int): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCrossRef(vararg crossRef: UserSearchedKeyCrossRefEntity)

    @Delete
    suspend fun deleteCrossRef(vararg crossRef: UserSearchedKeyCrossRefEntity)

    @Query("DELETE FROM user_searched_key_cross_ref WHERE key_id = :keyId")
    suspend fun remove(keyId: Long?)

    @Query("DELETE FROM user_searched_key_cross_ref")
    suspend fun clearAll()
}