package pro.branium.core_database.entity.paging

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_remote_keys")
data class ArtistRemoteKeysEntity(
    @PrimaryKey
    @ColumnInfo(name = "artist_id")
    val id: Int,

    @ColumnInfo(name = "prev_key")
    val prevKey: Int?,

    @ColumnInfo(name = "next_key")
    val nextKey: Int?
)
