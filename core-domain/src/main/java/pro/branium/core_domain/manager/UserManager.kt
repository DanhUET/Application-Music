package pro.branium.core_domain.manager

import pro.branium.core_model.UserProfile

interface UserManager {
    fun isLoggedIn(): Boolean
    fun getCurrentUserId(): Int      // Trả về "local" hoặc "guest" nếu chưa đăng nhập
    fun getCurrentUser(): UserProfile?  // Dữ liệu người dùng hiện tại (có thể null)
    fun login(userProfile: UserProfile)
    fun logout()
}