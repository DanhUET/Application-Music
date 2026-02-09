package pro.branium.core_database.entity.playlist

import androidx.room.ColumnInfo

data class PlaylistWithCountEntity(
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "artwork")
    val artwork: String?,
    @ColumnInfo(name = "song_count")
    val songCount: Int
)