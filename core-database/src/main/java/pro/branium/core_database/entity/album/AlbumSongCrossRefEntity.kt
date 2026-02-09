package pro.branium.core_database.entity.album

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "album_song_cross_ref",
    primaryKeys = ["song_id", "album_id"],
    indices = [Index("album_id"), Index("song_id")]
)
data class AlbumSongCrossRefEntity(
    @ColumnInfo(name = "song_id")
    val songId: String,

    @ColumnInfo(name = "album_id")
    val albumId: Int,

    @ColumnInfo(name = "created_at", defaultValue = "0")
    val createdAt: Long = System.currentTimeMillis()
)