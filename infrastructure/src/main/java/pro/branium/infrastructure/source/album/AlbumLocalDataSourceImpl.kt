package pro.branium.infrastructure.source.album

import androidx.paging.PagingSource
import pro.branium.core_database.dao.album.AlbumDao
import pro.branium.core_database.dao.album.AlbumSongCrossRefDao
import pro.branium.core_database.dao.song.SongDao
import pro.branium.core_database.entity.album.AlbumEntity
import pro.branium.core_database.entity.album.AlbumListItem
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.feature_album.data.datasource.AlbumLocalDataSource
import javax.inject.Inject

class AlbumLocalDataSourceImpl @Inject constructor(
    private val albumDao: AlbumDao,
    private val songDao: SongDao,
    private val albumSongCrossRefDao: AlbumSongCrossRefDao
) : AlbumLocalDataSource {
    override fun albumPagingSource(): PagingSource<Int, AlbumListItem> =
        albumDao.albumPagingSource()

    override fun getNAlbumPagingSource(limit: Int): PagingSource<Int, AlbumListItem> {
        return albumDao.getNAlbumPagingSource(limit)
    }

    override fun getTopAlbums(limit: Int): List<AlbumListItem> {
        return albumDao.getTopAlbums(limit)
    }

    override suspend fun getAlbumById(albumId: Int): AlbumEntity? {
        return albumDao.getAlbumById(albumId)
    }

    override suspend fun getSongsForAlbum(albumId: Int): List<SongEntity> {
//        val songIds = albumSongCrossRefDao.getSongIdsForAlbum(albumId)
//        return songDao.getSongsByIds(songIds)
        return albumDao.getAlbumWithSongs(albumId)?.songs ?: emptyList()
    }

    override suspend fun insert(vararg albums: AlbumEntity) {
        albumDao.insert(*albums)
    }
}