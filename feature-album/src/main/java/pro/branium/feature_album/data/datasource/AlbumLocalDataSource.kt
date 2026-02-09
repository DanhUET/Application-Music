package pro.branium.feature_album.data.datasource

import androidx.paging.PagingSource
import pro.branium.core_database.entity.album.AlbumEntity
import pro.branium.core_database.entity.album.AlbumListItem
import pro.branium.core_database.entity.song.SongEntity

interface AlbumLocalDataSource {
    fun albumPagingSource(): PagingSource<Int, AlbumListItem>

    fun getNAlbumPagingSource(limit: Int): PagingSource<Int, AlbumListItem>

    fun getTopAlbums(limit: Int): List<AlbumListItem>

    suspend fun getAlbumById(albumId: Int): AlbumEntity?

    suspend fun getSongsForAlbum(albumId: Int): List<SongEntity>

    suspend fun insert(vararg albums: AlbumEntity)
}