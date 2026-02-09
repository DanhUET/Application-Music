package pro.branium.core_database.entity.album

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey
    @ColumnInfo(name = "album_id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "artist_name", defaultValue = "Unknown")
    val artistName: String,

    @ColumnInfo(name = "release_date", defaultValue = "0")
    val releaseDate: Long,

    @ColumnInfo(name = "genre", defaultValue = "Unknown")
    val genre: String = "Unknown",

    @ColumnInfo(name = "album_type", defaultValue = "Unknown")
    val albumType: String,

    @ColumnInfo(name = "play_count", defaultValue = "0")
    val playCount: Int = 0,

    @ColumnInfo("artwork_url")
    val artworkUrl: String? = null,
)
