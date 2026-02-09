package pro.branium.feature_artist.data.dto

import com.google.gson.annotations.SerializedName

data class ArtistDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("avatar")
    val avatar: String?,

    @SerializedName("interested")
    val interested: Int,
)