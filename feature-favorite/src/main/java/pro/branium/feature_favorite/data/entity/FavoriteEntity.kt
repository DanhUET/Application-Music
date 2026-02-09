package pro.branium.feature_favorite.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "favorites",
    primaryKeys = ["user_id", "song_id"]
)
data class FavoriteEntity(
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "song_id")
    val songId: String
)