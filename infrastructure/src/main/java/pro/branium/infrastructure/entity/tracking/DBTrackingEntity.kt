package pro.branium.infrastructure.entity.tracking

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "db_tracking")
data class DBTrackingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "last_song_updated", defaultValue = "0")
    val lastSongUpdated: Long = 0,

    @ColumnInfo(name = "last_artist_updated", defaultValue = "0")
    val lastArtistUpdated: Long = 0,

    @ColumnInfo(name = "last_album_updated", defaultValue = "0")
    val lastAlbumUpdated: Long = 0
)