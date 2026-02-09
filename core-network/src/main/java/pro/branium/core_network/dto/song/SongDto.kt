package pro.branium.core_network.dto.song

import com.google.gson.annotations.SerializedName

data class SongDto(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("title")
    val title: String,

    @SerializedName("album")
    val album: String? = null,

    @SerializedName("artist")
    val artist: String,

    @SerializedName("source")
    val source: String,

    @SerializedName("image")
    val artworkUrl: String? = null,

    @SerializedName("duration")
    val durationSeconds: Int = 0,

    @SerializedName("counter")
    val counter: Int = 0,

    @SerializedName("track_number")
    val trackNumber: Int? = null,

    @SerializedName("genre")
    val genre: String? = null,

    @SerializedName("year")
    val year: Int? = null,

    @SerializedName("lyrics_url")
    val lyricsUrl: String? = null
)