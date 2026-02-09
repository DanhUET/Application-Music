package pro.branium.core_database.entity.album

import androidx.room.ColumnInfo

data class AlbumListItem(
    @ColumnInfo(name = "album_id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "artwork_url") val artworkUrl: String?
)