package pro.branium.feature_artist.data.api

import pro.branium.feature_artist.data.dto.ArtistDto
import pro.branium.feature_artist.data.dto.ArtistListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtistApi {
    @GET("services/services.php/artists")
    fun loadArtistPaging(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<ArtistListDto>

    @GET("services/services.php/artists/{artistId}")
    fun getArtistById(@Path("artistId") artistId: Int): Call<ArtistDto?>

    @GET("services/services.php/artists/{artistName}")
    fun getArtistByName(@Path("artistName") artistName: String): Call<ArtistDto?>
}