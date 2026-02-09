package pro.branium.core_network.dto.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("username")
    val username: String = "",
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("email")
    val email: String = "",
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("createdAt")
    val createdAt: Long = 0,
    @SerializedName("avatar")
    val avatar: String? = null
)
