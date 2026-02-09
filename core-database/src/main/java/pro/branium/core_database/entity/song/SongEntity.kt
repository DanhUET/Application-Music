package pro.branium.core_database.entity.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey
    @ColumnInfo(name = "song_id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "album")
    val album: String? = null,

    @ColumnInfo(name = "artist")
    val artist: String = "Unknown",

    @ColumnInfo(name = "source")
    val source: String,

    @ColumnInfo(name = "image")
    val imageArtworkUrl: String = "",

    @ColumnInfo(name = "duration")
    val durationSeconds: Int = 0,

    @ColumnInfo(name = "counter")
    val counter: Int = 0,

    @ColumnInfo(name = "track_number")
    val trackNumber: Int = 0,

    @ColumnInfo(name = "genre", defaultValue = "Pop")
    val genre: String? = null,

    @ColumnInfo(name = "year", defaultValue = "null")
    val year: Int? = null,

    @ColumnInfo(name = "lyrics_url", defaultValue = "null")
    val lyricsUrl: String? = null
)