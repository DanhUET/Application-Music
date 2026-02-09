package pro.branium.feature_user.domain.model 

data class UserModel(
    val userId: Int,
    val username: String,
    val email: String = "",
    val phoneNumber: String? = null,
    val password: String? = null,
    val avatar: String? = null,
    val isLoggedIn: Boolean = false,
    val createdAt: Long = 0
)