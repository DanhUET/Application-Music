package pro.branium.core_network.dto.playlist

import com.google.gson.annotations.SerializedName

data class PlaylistDto(
    @SerializedName("playlistId")
    var playlistId: Long = System.currentTimeMillis(),

    @SerializedName("name")
    var name: String = "",

    @SerializedName("artwork")
    var artwork: String? = null,

    @SerializedName("createdAt")
    var createdAt: Long = System.currentTimeMillis()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlaylistDto) return false

        if (playlistId != other.playlistId) return false

        return true
    }

    override fun hashCode(): Int {
        return playlistId.hashCode()
    }
}
