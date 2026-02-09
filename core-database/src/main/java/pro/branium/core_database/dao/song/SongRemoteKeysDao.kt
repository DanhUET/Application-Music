package pro.branium.core_database.dao.song

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.branium.core_database.entity.paging.SongRemoteKeysEntity

@Dao
interface SongRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(remoteKey: List<SongRemoteKeysEntity>)

    @Query("SELECT * FROM song_remote_keys WHERE song_id = :songId")
    suspend fun remoteKeysSongId(songId: String): SongRemoteKeysEntity?

    @Query("DELETE FROM song_remote_keys")
    suspend fun clearAll()
}