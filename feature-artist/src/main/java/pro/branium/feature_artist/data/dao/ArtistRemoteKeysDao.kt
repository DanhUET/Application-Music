package pro.branium.feature_artist.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.branium.core_database.entity.paging.ArtistRemoteKeysEntity

@Dao
interface ArtistRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ArtistRemoteKeysEntity>)

    @Query("SELECT * FROM artist_remote_keys WHERE artist_id = :artistId")
    suspend fun remoteKeysArtistId(artistId: Int): ArtistRemoteKeysEntity?

    @Query("DELETE FROM artist_remote_keys")
    suspend fun clearAll()
}