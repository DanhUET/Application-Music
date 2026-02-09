package pro.branium.infrastructure.source.searching

import pro.branium.core_network.dto.song.SongDto
import pro.branium.core_network.dto.song.SongListDto
import pro.branium.feature.search.data.datasource.SearchingRemoteDataSource
import pro.branium.feature_song.data.SongApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SearchingRemoteDataSourceImpl @Inject constructor(
    private val songApi: SongApi
) : SearchingRemoteDataSource {
    override suspend fun search(query: String): List<SongDto> {
        val call = songApi.search(query)
        return executeTask(call)
    }

    private suspend fun executeTask(call: Call<SongListDto>): List<SongDto> {
        return suspendCoroutine { continuation ->
            call.enqueue(object : Callback<SongListDto> {
                override fun onResponse(
                    call: Call<SongListDto?>,
                    response: Response<SongListDto?>
                ) {
                    if (response.isSuccessful) {
                        val songs = response.body()?.songListDto
                        if (songs != null) {
                            continuation.resume(songs)
                        } else {
                            continuation.resumeWithException(Exception("Response body is null"))
                        }
                    } else {
                        continuation.resumeWithException(Exception(response.message()))
                    }
                }

                override fun onFailure(
                    call: Call<SongListDto?>,
                    throwable: Throwable
                ) {
                    continuation.resumeWithException(throwable)
                }
            })
        }
    }
}