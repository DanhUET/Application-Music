package pro.branium.core_network.dto.album

import com.google.gson.annotations.SerializedName
import pro.branium.core_network.dto.song.SongDto

data class AlbumDto(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("artistName")
    val artistName: String?,

    @SerializedName("releaseDate")
    val releaseDate: Long?,

    @SerializedName("genre")
    val genre: String?,

    @SerializedName("albumType")
    val albumType: String?,

    @SerializedName("playCount")
    val playCount: Int?,

    @SerializedName("songs")
    val songs: List<SongDto> = emptyList(),

    @SerializedName("artwork")
    val artworkUrl: String? = null
)
