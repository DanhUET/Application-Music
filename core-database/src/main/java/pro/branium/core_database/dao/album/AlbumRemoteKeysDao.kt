package pro.branium.core_database.dao.album

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.branium.core_database.entity.paging.AlbumRemoteKeysEntity

@Dao
interface AlbumRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(remoteKey: List<AlbumRemoteKeysEntity>)

    @Query("SELECT * FROM album_remote_keys WHERE album_id = :albumId")
    suspend fun remoteKeyAlbumId(albumId: Int): AlbumRemoteKeysEntity?

    @Query("DELETE FROM album_remote_keys")
    suspend fun clearAll()
}