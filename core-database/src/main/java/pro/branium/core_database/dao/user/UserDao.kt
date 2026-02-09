package pro.branium.core_database.dao.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.user.UserEntity

@Dao
interface UserDao {
    @Query(
        "SELECT user_id, username, email, password, phone_number, created_at, avatar " +
                "FROM users " +
                "WHERE user_id = :userId"
    )
    fun getUser(userId: Int): Flow<UserEntity?>

    @Query(
        "SELECT user_id, username, email, password, phone_number, created_at, avatar " +
                "FROM users " +
                "WHERE (email = :email AND password = :password) " +
                "OR (phone_number = :phoneNumber AND password = :password)"
    )
    suspend fun login(email: String, phoneNumber: String, password: String): UserEntity?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun countUser(): Int

    @Insert
    suspend fun insert(userModel: UserEntity): Long

    @Delete
    suspend fun delete(userModel: UserEntity): Int

    @Update
    suspend fun update(userModel: UserEntity): Int
}