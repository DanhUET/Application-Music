package pro.branium.core_network.dto.song

import com.google.gson.annotations.SerializedName

data class SongListDto(
    @SerializedName("songs")
    val songListDto: List<SongDto> = emptyList()
)
