package pro.branium.infrastructure.source.album

import pro.branium.core_network.dto.album.AlbumDto
import pro.branium.core_network.dto.album.AlbumListDto
import pro.branium.core_network.dto.song.SongListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumApi {
    @GET("/services/services.php/albums")
    fun loadAlbumPaging(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<AlbumListDto>

    @GET("/services/services.php/albums/{albumId}")
    fun getAlbumById(@Path("albumId") albumId: Int): Call<AlbumDto?>

    @GET("/services/services.php/albums/{albumId}/songs")
    fun getSongsForAlbum(@Path("albumId") albumId: Int): Call<SongListDto>
}