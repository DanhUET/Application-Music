package pro.branium.feature_album.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.SongModel
import pro.branium.core_model.AlbumModel
import pro.branium.feature_album.domain.model.AlbumSummaryModel

interface AlbumRepository {
    val albumPagingSource: Flow<PagingData<AlbumSummaryModel>>

    fun getNAlbumPagingSource(limit: Int): Flow<PagingData<AlbumSummaryModel>>

    suspend fun getTopAlbums(limit: Int): List<AlbumSummaryModel>

    suspend fun getAlbumById(albumId: Int): AlbumModel?

    suspend fun getSongsForAlbum(albumId: Int): Result<List<SongModel>>

    suspend fun insertAlbum(vararg albums: AlbumModel)
}