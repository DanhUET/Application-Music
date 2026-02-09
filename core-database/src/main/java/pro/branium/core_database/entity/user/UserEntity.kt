package pro.branium.core_database.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id")
    val userId: Int = 0,

    @ColumnInfo("username")
    val username: String = "",

    @ColumnInfo("password")
    val password: String? = null,

    @ColumnInfo("email")
    val email: String = "",

    @ColumnInfo("phone_number")
    val phoneNumber: String? = null,

    @ColumnInfo("created_at")
    val createdAt: Long = 0,

    @ColumnInfo("avatar")
    val avatar: String? = null
)
