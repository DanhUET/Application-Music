package pro.branium.infrastructure.db.dao.tracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pro.branium.infrastructure.entity.tracking.DBTrackingEntity

@Dao
interface DBTrackingDao {
    @get:Query("SELECT * FROM db_tracking WHERE last_song_updated <> 0 " +
            "ORDER BY last_song_updated DESC LIMIT 1")
    val songTracking: DBTrackingEntity?

    @get:Query("SELECT * FROM db_tracking WHERE last_artist_updated <> 0 " +
            "ORDER BY last_artist_updated DESC LIMIT 1")
    val artistTracking: DBTrackingEntity?

    @get:Query("SELECT * FROM db_tracking WHERE last_album_updated <> 0 " +
            "ORDER BY last_album_updated DESC LIMIT 1")
    val albumTracking: DBTrackingEntity?

    @Insert
    suspend fun insert(tracking: DBTrackingEntity)

    @Query("DELETE FROM db_tracking")
    suspend fun clearAll()
}