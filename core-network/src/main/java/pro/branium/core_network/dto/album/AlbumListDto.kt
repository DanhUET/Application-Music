package pro.branium.core_network.dto.album

import com.google.gson.annotations.SerializedName

data class AlbumListDto(
    @SerializedName("albums")
    val albumListDto: List<AlbumDto> = emptyList()
)
