package pro.branium.core_database.entity.paging

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_remote_keys")
data class SongRemoteKeysEntity(
    @PrimaryKey
    @ColumnInfo(name = "song_id")
    val songId: String,

    @ColumnInfo(name = "prev_key")
    val prevKey: Int?,

    @ColumnInfo(name = "next_key")
    val nextKey: Int?
)
