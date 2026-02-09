package pro.branium.infrastructure.source.album

import pro.branium.core_network.dto.album.AlbumDto
import pro.branium.core_network.dto.album.AlbumListDto
import pro.branium.core_network.dto.paging_param.PagingParamRequest
import pro.branium.core_network.dto.song.SongListDto
import pro.branium.feature_album.data.datasource.AlbumRemoteDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AlbumRemoteDataSourceImpl @Inject constructor(
    private val albumApi: AlbumApi
) : AlbumRemoteDataSource {
    override suspend fun loadAlbumPaging(param: PagingParamRequest): List<AlbumDto> {
        val call = albumApi.loadAlbumPaging(param.limit, param.offset)
        return suspendCoroutine { continuation ->
            call.enqueue(object : Callback<AlbumListDto> {
                override fun onResponse(
                    call: Call<AlbumListDto?>,
                    response: Response<AlbumListDto?>
                ) {
                    if (response.isSuccessful) {
                        val albums = response.body()?.albumListDto
                        if (albums != null) {
                            continuation.resume(albums)
                        } else {
                            continuation.resumeWithException(Exception("Response body is null"))
                        }
                    } else {
                        continuation.resumeWithException(Exception(response.message()))
                    }
                }

                override fun onFailure(
                    call: Call<AlbumListDto?>,
                    throwable: Throwable
                ) {
                    continuation.resumeWithException(throwable)
                }
            })
        }
    }

    override suspend fun getAlbumById(albumId: Int): AlbumDto? {
        val call = albumApi.getAlbumById(albumId)
        return suspendCoroutine { continuation ->
            call.enqueue(object : Callback<AlbumDto?> {
                override fun onResponse(call: Call<AlbumDto?>, response: Response<AlbumDto?>) {
                    if (response.isSuccessful) {
                        val album = response.body()
                        continuation.resume(album)
                    } else {
                        continuation.resumeWithException(Exception(response.message()))
                    }
                }

                override fun onFailure(call: Call<AlbumDto?>, throwable: Throwable) {
                    return continuation.resumeWithException(throwable)
                }
            })
        }
    }

    override suspend fun getSongsForAlbum(albumId: Int): SongListDto {
        val call = albumApi.getSongsForAlbum(albumId)
        return suspendCoroutine { continuation ->
            call.enqueue(object : Callback<SongListDto> {
                override fun onResponse(call: Call<SongListDto>, response: Response<SongListDto>) {
                    if (response.isSuccessful) {
                        val songListDto = response.body()
                        if (songListDto != null) {
                            continuation.resume(songListDto)
                        } else {
                            continuation.resumeWithException(Exception("Response body is null"))
                        }
                    }
                }

                override fun onFailure(call: Call<SongListDto>, throwable: Throwable) {
                    return continuation.resumeWithException(throwable)
                }
            })
        }
    }
}