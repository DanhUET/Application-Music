package pro.branium.core_database.entity.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "playlist_song_cross_ref",
    primaryKeys = ["playlist_id", "song_id"],
    indices = [Index("song_id"), Index("playlist_id")]
)
data class PlaylistSongCrossRefEntity(
    @ColumnInfo(name = "playlist_id")
    var playlistId: Long = 0,

    @ColumnInfo(name = "song_id")
    var songId: String = "",

    @ColumnInfo(name = "created_at", defaultValue = "0")
    val createdAt: Long = System.currentTimeMillis()
)