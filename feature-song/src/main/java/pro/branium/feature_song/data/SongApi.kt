package pro.branium.feature_song.data

import pro.branium.core_network.dto.song.SongDto
import pro.branium.core_network.dto.song.SongListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SongApi {
    @GET("services/services.php/songs")
    fun loadSongPaging(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<SongListDto>

    @POST("/services/services.php")
    suspend fun updateCounter(
        @Query("queryType") queryType: String = "updateCounter",
        @Query("songId") songId: String
    )

    @GET("/services/services.php/search")
    fun search(@Query("query") query: String): Call<SongListDto>

    @GET("/services/services.php/firstNSongs")
    fun getFirstNSongs(
        @Query("playlistId") playlistId: Int,
        @Query("limit") limit: Int = 10
    ): Call<SongListDto>

    @GET("/services/services.php/previous")
    fun getPreviousNSongs(
        @Query("playlistId") playlistId: Int,
        @Query("songId") songId: String,
        @Query("limit") limit: Int = 10
    ): Call<SongListDto>

    @GET("/services/services.php/next")
    fun getNextNSongs(
        @Query("playlistId") playlistId: Int,
        @Query("songId") songId: String,
        @Query("limit") limit: Int = 10
    ): Call<SongListDto>

    @GET("/services/services.php/lastNSongs")
    fun getLastNSongs(
        @Query("playlistId") playlistId: Int,
        @Query("limit") limit: Int = 10
    ): Call<SongListDto>

    @GET("/services/services.php/getSongById")
    fun getSongById(
        @Query("songId") songId: String,
        @Query("playlistId") playlistId: Int
    ): Call<SongDto?>

    @GET("/services/services.php/recentSongs")
    fun getRecentSongs(
        @Query("userId") userId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 10
    ): Call<SongListDto>

    @PUT("/services/services.php/recentSongs")
    suspend fun setRecentSong(
        @Query("userId") userId: Int,
        @Query("songId") songId: String,
        @Query("createdAt") createdAt: Long
    )

    @GET("services/services.php/artists/{artistId}/songs")
    fun getSongsByArtist(@Path("artistId") artistId: Int): Call<SongListDto>
}