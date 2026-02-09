package pro.branium.core_database.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "user_searched_song_cross_ref",
    primaryKeys = ["user_id", "song_id"],
    indices = [Index("user_id"), Index("song_id")]
)
data class UserSearchedSongCrossRefEntity(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "song_id") val songId: String,
    @ColumnInfo(name = "created_at") val selectedAt: Long = System.currentTimeMillis()
)