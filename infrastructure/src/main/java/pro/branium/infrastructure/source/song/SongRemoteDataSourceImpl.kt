package pro.branium.infrastructure.source.song

import pro.branium.core_network.dto.paging_param.PagingParamRequest
import pro.branium.core_network.dto.song.SongDto
import pro.branium.core_network.dto.song.SongListDto
import pro.branium.feature_song.data.SongApi
import pro.branium.feature_song.data.datasource.SongRemoteDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SongRemoteDataSourceImpl @Inject constructor(
    private val songApi: SongApi
) : SongRemoteDataSource {
    override suspend fun loadSongPaging(params: PagingParamRequest): List<SongDto> {
        val call = songApi.loadSongPaging(params.limit, params.offset)
        return executeTask(call)
    }

    override suspend fun loadFirstNSongs(playlistId: Long, limit: Int): List<SongDto> {
        val call = songApi.getFirstNSongs(playlistId.toInt(), limit)
        return executeTask(call)
    }

    override suspend fun loadPreviousNSongs(
        playlistId: Int,
        songId: String,
        limit: Int
    ): List<SongDto> {
        val call = songApi.getPreviousNSongs(playlistId, songId, limit)
        return executeTask(call)
    }

    override suspend fun loadNextNSongs(
        playlistId: Int,
        songId: String,
        limit: Int
    ): List<SongDto> {
        val call = songApi.getNextNSongs(playlistId, songId, limit)
        return executeTask(call)
    }

    override suspend fun loadLastNSongs(playlistId: Long, limit: Int): List<SongDto> {
        val call = songApi.getLastNSongs(playlistId.toInt(), limit)
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

    override suspend fun getSongById(songId: String, playlistId: Long?): SongDto? {
        val call = songApi.getSongById(songId, playlistId?.toInt() ?: 0)
        return suspendCoroutine { continuation ->
            call.enqueue(object : Callback<SongDto?> {
                override fun onResponse(
                    call: Call<SongDto?>,
                    response: Response<SongDto?>
                ) {
                    if (response.isSuccessful) {
                        val song = response.body()
                        if (song != null) {
                            continuation.resume(song)
                        } else {
                            continuation.resumeWithException(Exception("Response body is null"))
                        }
                    } else {
                        continuation.resumeWithException(Exception(response.message()))
                    }
                }

                override fun onFailure(
                    call: Call<SongDto?>,
                    throwable: Throwable
                ) {
                    continuation.resumeWithException(throwable)
                }
            })
        }
    }

    override suspend fun getFavoriteSongs(limit: Int): List<SongDto> {
        // todo: get favorite songs from remote
        return emptyList()
    }

    override suspend fun getSongsByArtist(artistId: Int): SongListDto {
        val call = songApi.getSongsByArtist(artistId)
        return suspendCoroutine { continuation ->
            call.enqueue(object : Callback<SongListDto> {
                override fun onResponse(
                    call: Call<SongListDto?>,
                    response: Response<SongListDto?>
                ) {
                    if (response.isSuccessful) {
                        val songList = response.body()
                        if (songList != null) {
                            continuation.resume(songList)
                        } else {
                            continuation.resumeWithException(Exception("Artists is null"))
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