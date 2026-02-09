package pro.branium.feature_song.data.datasource

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity

interface SongLocalDataSource {
    fun getMostHeardSongs(limit: Int): Flow<List<SongEntity>>

    fun songPagingSource(): PagingSource<Int, SongEntity>

    suspend fun getSongsInAlbum(album: String): List<SongEntity>

    suspend fun getSongsInPlaylist(playlistId: Long): List<SongEntity>

    fun getNSongPagingSource(limit: Int): PagingSource<Int, SongEntity>

    suspend fun getSongById(id: String): SongEntity?

    suspend fun insert(vararg song: SongEntity)

    suspend fun updateSongCounter(songId: String)

    suspend fun getSongsByArtist(artistId: Int): List<SongEntity>
}