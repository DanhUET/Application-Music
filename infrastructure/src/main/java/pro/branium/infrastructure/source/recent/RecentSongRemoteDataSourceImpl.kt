package pro.branium.infrastructure.source.recent

import pro.branium.core_network.dto.song.SongDto
import pro.branium.core_network.dto.song.SongListDto
import pro.branium.feature_recent.data.datasource.RecentSongRemoteDataSource
import pro.branium.feature_song.data.SongApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecentSongRemoteDataSourceImpl @Inject constructor(
    private val songApi: SongApi
) : RecentSongRemoteDataSource {
    override suspend fun getRecentSongs(userId: Int, limit: Int, offset: Int): List<SongDto> {
        val call = songApi.getRecentSongs(userId, limit, offset)
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

    override suspend fun setRecentSong(userId: Int, songId: String, createdAt: Long) {
        songApi.setRecentSong(userId, songId, createdAt)
    }
}