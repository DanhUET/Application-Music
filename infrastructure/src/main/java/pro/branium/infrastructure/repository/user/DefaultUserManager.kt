package pro.branium.infrastructure.repository.user

import android.content.SharedPreferences
import androidx.core.content.edit
import pro.branium.core_domain.manager.UserManager
import pro.branium.core_model.UserProfile
import javax.inject.Inject

class DefaultUserManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserManager {

    companion object {
        private const val PREF_USER_ID = "pref_user_id"
    }

    override fun isLoggedIn(): Boolean {
        return getCurrentUserId() != 0
    }

    override fun getCurrentUserId(): Int {
        return sharedPreferences.getInt(PREF_USER_ID, 0)
    }

    override fun getCurrentUser(): UserProfile? {
        // Có thể đọc từ SharedPreferences hoặc database nếu có
        val id = getCurrentUserId()
        return if (id == 0) null else UserProfile(id) // Ví dụ, chỉ trả về id
    }

    override fun login(userProfile: UserProfile) {
        sharedPreferences.edit { putInt(PREF_USER_ID, userProfile.id) }
        // Lưu thêm thông tin khác nếu cần
    }

    override fun logout() {
        sharedPreferences.edit { putInt(PREF_USER_ID, 0) }
        // Xoá thêm thông tin khác nếu cần
    }
}