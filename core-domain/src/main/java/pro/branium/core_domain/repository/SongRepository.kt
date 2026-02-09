package pro.branium.core_domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.SongModel

interface SongRepository {
    fun getMostHeardSongs(limit: Int): Flow<List<SongModel>>

    suspend fun getForYouSongs(limit: Int): List<SongModel>

    val songModelPagingSource: Flow<PagingData<SongModel>>

    suspend fun insert(vararg songModel: SongModel)

    suspend fun updateSongCounter(songId: String)

    suspend fun getSongsInAlbum(album: String): List<SongModel>

    suspend fun getSongsInPlaylist(playlistId: Long): List<SongModel>

    fun getLimitedSongsPaging(limit: Int): Flow<PagingData<SongModel>>

    suspend fun getSongById(songId: String, playlistId: Long? = 1): SongModel?

    suspend fun getSongsByArtist(artistId: Int): List<SongModel>
}