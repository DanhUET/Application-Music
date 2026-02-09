package pro.branium.core_database.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "user_searched_key_cross_ref",
    primaryKeys = ["user_id", "key_id"],
    indices = [Index(value = ["user_id", "key_id"])]
)
data class UserSearchedKeyCrossRefEntity(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "key_id") val keyId: Long,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)