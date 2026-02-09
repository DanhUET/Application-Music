package pro.branium.infrastructure.repository.album

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.branium.core_model.AlbumModel
import pro.branium.core_model.SongModel
import pro.branium.core_network.dto.paging_param.PagingParamRequest
import pro.branium.core_utils.MusicAppUtils
import pro.branium.feature_album.data.datasource.AlbumLocalDataSource
import pro.branium.feature_album.data.datasource.AlbumRemoteDataSource
import pro.branium.feature_album.domain.model.AlbumSummaryModel
import pro.branium.feature_album.domain.repository.AlbumRepository
import pro.branium.infrastructure.mapper.local.songEntitiesToModels
import pro.branium.infrastructure.mapper.local.toAlbumSummaryModel
import pro.branium.infrastructure.mapper.local.toAlbumSummaryModelList
import pro.branium.infrastructure.mapper.local.toEntity
import pro.branium.infrastructure.mapper.local.toModel
import pro.branium.infrastructure.mapper.remote.toAlbumSummaryModels
import pro.branium.infrastructure.mapper.remote.toModel
import pro.branium.infrastructure.mapper.remote.toModels
import pro.branium.infrastructure.source.mediator.AlbumRemoteMediator
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumLocalDataSource: AlbumLocalDataSource,
    private val albumRemoteDataSource: AlbumRemoteDataSource,
    private val albumRemoteMediator: AlbumRemoteMediator
) : AlbumRepository {
    @OptIn(ExperimentalPagingApi::class)
    override val albumPagingSource: Flow<PagingData<AlbumSummaryModel>> = Pager(
        config = PagingConfig(MusicAppUtils.ALBUM_PAGE_SIZE),
        remoteMediator = albumRemoteMediator
    ) {
        albumLocalDataSource.albumPagingSource()
    }.flow
        .map { pagingData ->
            pagingData.map { albumListItem -> albumListItem.toAlbumSummaryModel() }
        }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNAlbumPagingSource(limit: Int): Flow<PagingData<AlbumSummaryModel>> = Pager(
        config = PagingConfig(limit),
        remoteMediator = albumRemoteMediator
    ) {
        albumLocalDataSource.getNAlbumPagingSource(limit)
    }.flow
        .map { pagingData ->
            pagingData.map { albumListItem -> albumListItem.toAlbumSummaryModel() }
        }

    override suspend fun getTopAlbums(limit: Int): List<AlbumSummaryModel> {
        val localAlbums = albumLocalDataSource.getTopAlbums(limit)
        return if (localAlbums.isNotEmpty()) {
            localAlbums.toAlbumSummaryModelList()
        } else {
            return try {
                val page = PagingParamRequest(limit = limit, offset = 0)
                val albumListDto = albumRemoteDataSource.loadAlbumPaging(page)
                insertAlbum(*albumListDto.toModels().toTypedArray())
                albumListDto.toAlbumSummaryModels()
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getAlbumById(albumId: Int): AlbumModel? {
        val localAlbum = albumLocalDataSource.getAlbumById(albumId)
        return localAlbum?.toModel() ?: try {
            albumRemoteDataSource.getAlbumById(albumId)?.toModel()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getSongsForAlbum(albumId: Int): Result<List<SongModel>> {
        val localSongs = albumLocalDataSource.getSongsForAlbum(albumId)
        return if (localSongs.isNotEmpty()) {
            Result.success(localSongs.songEntitiesToModels())
        } else {
            try {
                val songListDto = albumRemoteDataSource.getSongsForAlbum(albumId)
                val songModelList = songListDto.songListDto.toModels()
                Result.success(songModelList)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun insertAlbum(vararg albums: AlbumModel) {
        val albumEntities = albums.map {
            it.toEntity()
        }
        albumLocalDataSource.insert(*albumEntities.toTypedArray())
    }
}