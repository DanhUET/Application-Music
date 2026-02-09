package pro.branium.core_model

data class UserProfile(
    val id: Int,
    val displayName: String? = null,
    val email: String? = null,
    val avatarUrl: String? = null,
    // Thêm các trường khác nếu cần
)