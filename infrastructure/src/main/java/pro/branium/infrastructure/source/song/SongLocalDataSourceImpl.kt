package pro.branium.infrastructure.source.song

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.dao.song.SongDao
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.feature_song.data.datasource.SongLocalDataSource
import javax.inject.Inject

class SongLocalDataSourceImpl @Inject constructor(
    private val songDao: SongDao
) : SongLocalDataSource {
    override fun getMostHeardSongs(limit: Int): Flow<List<SongEntity>> {
        return songDao.getMostHeardSongs(limit)
    }

    override fun songPagingSource(): PagingSource<Int, SongEntity> =
        songDao.songPagingSource()

    override suspend fun getSongsInAlbum(album: String): List<SongEntity> {
        return songDao.getSongsInAlbum(album)
    }

    override suspend fun getSongsInPlaylist(playlistId: Long): List<SongEntity> {
        return when (playlistId) {
            1L -> {
                songDao.getLimitedSongs(20)
            }
            9L -> {
                songDao.getAllSongs()
            }
            else -> {
                songDao.getSongsInPlaylist(playlistId)
            }
        }
    }

    override fun getNSongPagingSource(limit: Int): PagingSource<Int, SongEntity> {
        return songDao.getNSongPagingSource(limit)
    }

    override suspend fun getSongById(id: String): SongEntity? {
        return songDao.getSongById(id)
    }

    override suspend fun insert(vararg song: SongEntity) {
        songDao.insert(*song)
    }

    override suspend fun updateSongCounter(songId: String) {
        songDao.updateSongCounter(songId)
    }

    override suspend fun getSongsByArtist(artistId: Int): List<SongEntity> {
        return songDao.getSongByArtist(artistId)
    }
}