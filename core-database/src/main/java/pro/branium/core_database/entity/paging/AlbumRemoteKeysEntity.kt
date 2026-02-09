package pro.branium.core_database.entity.paging

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_remote_keys")
data class AlbumRemoteKeysEntity(
    @PrimaryKey
    @ColumnInfo(name = "album_id")
    val id: Int,

    @ColumnInfo(name = "prev_page")
    val prevKey: Int?,

    @ColumnInfo(name = "next_page")
    val nextKey: Int?
)
