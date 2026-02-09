package pro.branium.feature_artist.data.dto

import com.google.gson.annotations.SerializedName

data class ArtistListDto(
    @SerializedName("artists") val artistListDto: List<ArtistDto>
)