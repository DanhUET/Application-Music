package pro.branium.feature_song.data.datasource

import pro.branium.core_network.dto.paging_param.PagingParamRequest
import pro.branium.core_network.dto.song.SongDto
import pro.branium.core_network.dto.song.SongListDto

interface SongRemoteDataSource {
    suspend fun loadSongPaging(params: PagingParamRequest): List<SongDto>

    suspend fun loadFirstNSongs(playlistId: Long, limit: Int): List<SongDto>

    suspend fun loadPreviousNSongs(playlistId: Int, songId: String, limit: Int): List<SongDto>

    suspend fun loadNextNSongs(playlistId: Int, songId: String, limit: Int): List<SongDto>

    suspend fun loadLastNSongs(playlistId: Long, limit: Int): List<SongDto>

    suspend fun getSongById(songId: String, playlistId: Long?): SongDto?

    suspend fun getFavoriteSongs(limit: Int): List<SongDto>

    suspend fun getSongsByArtist(artistId: Int): SongListDto
}