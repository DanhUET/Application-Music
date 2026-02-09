package pro.branium.core_database.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "user_song_cross_ref",
    primaryKeys = ["song_id", "user_id"],
    indices = [Index("user_id"), Index("song_id")]
)
data class UserSongCrossRefEntity(
    @ColumnInfo(name = "song_id")
    val songId: String,

    @ColumnInfo(name = "user_id")
    val userId: Int = 0,

    @ColumnInfo(name = "created_at", defaultValue = "0")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at", defaultValue = "0")
    val updatedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "replay", defaultValue = "1")
    val replay: Int = 1
)