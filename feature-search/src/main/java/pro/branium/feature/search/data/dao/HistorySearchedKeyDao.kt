package pro.branium.feature.search.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pro.branium.feature.search.data.entity.HistorySearchedKeyEntity

@Dao
interface HistorySearchedKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: HistorySearchedKeyEntity): Long

    @Query(
        "SELECT * FROM history_searched_keys WHERE id IN (:ids)" +
                "ORDER BY (" +
                "SELECT created_at FROM user_searched_key_cross_ref WHERE id = key_id" +
                ") DESC"
    )
    fun getHistorySearchedKeysForUserByIds(ids: List<Int>): Flow<List<HistorySearchedKeyEntity>>


    @Query("SELECT id FROM history_searched_keys WHERE `key` = :key")
    suspend fun getIdByKeyword(key: String): Long?

    @Query("DELETE FROM history_searched_keys WHERE `key` = :key")
    suspend fun deleteByKeyword(key: String): Int


    @Transaction
    suspend fun removeKey(key: String): Long? {
        val id = getIdByKeyword(key) ?: return null
        deleteByKeyword(key)
        return id
    }

    @Query("DELETE FROM history_searched_keys")
    suspend fun clearAllKeys()
}