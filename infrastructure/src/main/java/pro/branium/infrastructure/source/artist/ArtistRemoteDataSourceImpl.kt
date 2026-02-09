package pro.branium.infrastructure.source.artist

import pro.branium.feature_artist.data.api.ArtistApi
import pro.branium.feature_artist.data.datasource.ArtistRemoteDataSource
import pro.branium.feature_artist.data.dto.ArtistDto
import pro.branium.feature_artist.data.dto.ArtistListDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ArtistRemoteDataSourceImpl @Inject constructor(
    private val service: ArtistApi
) : ArtistRemoteDataSource {
    override suspend fun loadArtistPaging(limit: Int, offset: Int): List<ArtistDto> {
        val call = service.loadArtistPaging(limit, offset)
        return suspendCoroutine { continuation ->
            call.enqueue(object : Callback<ArtistListDto> {
                override fun onResponse(
                    call: Call<ArtistListDto?>,
                    response: Response<ArtistListDto?>
                ) {
                    if (response.isSuccessful) {
                        val artists = response.body()?.artistListDto
                        if (artists != null) {
                            continuation.resume(artists)
                        } else {
                            continuation.resumeWithException(Exception("Artists is null"))
                        }
                    } else {
                        continuation.resumeWithException(Exception(response.message()))
                    }
                }

                override fun onFailure(
                    call: Call<ArtistListDto?>,
                    throwable: Throwable
                ) {
                    continuation.resumeWithException(throwable)
                }
            })
        }
    }

    override suspend fun getArtistById(artistId: Int): ArtistDto? {
        val call = service.getArtistById(artistId)
        return suspendCoroutine { continuation ->
            call.enqueue(object : Callback<ArtistDto?> {
                override fun onResponse(
                    call: Call<ArtistDto?>,
                    response: Response<ArtistDto?>
                ) {
                    if (response.isSuccessful) {
                        val artist = response.body()
                        continuation.resume(artist)
                    } else {
                        continuation.resumeWithException(Exception(response.message()))
                    }
                }

                override fun onFailure(
                    call: Call<ArtistDto?>,
                    throwable: Throwable
                ) {
                    continuation.resumeWithException(throwable)
                }
            })
        }
    }

    override suspend fun getArtistByName(artistName: String): ArtistDto? {
        val call = service.getArtistByName(artistName)
        return suspendCoroutine { continuation ->
            call.enqueue(object : Callback<ArtistDto?> {
                override fun onResponse(
                    call: Call<ArtistDto?>,
                    response: Response<ArtistDto?>
                ) {
                    if (response.isSuccessful) {
                        val artist = response.body()
                        continuation.resume(artist)
                    } else {
                        continuation.resumeWithException(Exception(response.message()))
                    }
                }

                override fun onFailure(
                    call: Call<ArtistDto?>,
                    throwable: Throwable
                ) {
                    continuation.resumeWithException(throwable)
                }
            })
        }
    }
}