package pro.branium.infrastructure.source.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pro.branium.core_database.entity.album.AlbumEntity
import pro.branium.core_database.entity.album.AlbumListItem
import pro.branium.core_database.entity.paging.AlbumRemoteKeysEntity
import pro.branium.core_network.dto.paging_param.PagingParamRequest
import pro.branium.core_utils.MusicAppUtils.CACHE_TIMEOUT
import pro.branium.feature_album.data.datasource.AlbumRemoteDataSource
import pro.branium.infrastructure.db.AppDatabase
import pro.branium.infrastructure.entity.tracking.DBTrackingEntity
import pro.branium.infrastructure.mapper.local.toEntities
import pro.branium.infrastructure.mapper.remote.toModels
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class AlbumRemoteMediator @Inject constructor(
    private val database: AppDatabase,
    private val remoteDataSource: AlbumRemoteDataSource
) : RemoteMediator<Int, AlbumListItem>() {
    private val albumDao = database.albumDao()
    private val albumRemoteKeysDao = database.albumRemoteKeysDao()
    private val dbTrackingDao = database.dbTrackingDao()
    private var currentPosition = -1

    override suspend fun initialize(): InitializeAction {
        return withContext(Dispatchers.IO) {
            val lastAlbumUpdated = dbTrackingDao.albumTracking?.lastAlbumUpdated ?: 0
            val cacheTimeout = TimeUnit.MILLISECONDS.convert(CACHE_TIMEOUT, TimeUnit.HOURS)
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastAlbumUpdated < cacheTimeout) {
                InitializeAction.SKIP_INITIAL_REFRESH
            } else {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AlbumListItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 0
            }

            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        val pagingParamRequest = PagingParamRequest(
            page * state.config.pageSize,
            state.config.pageSize
        )
        if (pagingParamRequest.offset == currentPosition) {
            return MediatorResult.Success(endOfPaginationReached = true)
        } else {
            currentPosition = pagingParamRequest.offset
        }
        return try {
            val albums = remoteDataSource.loadAlbumPaging(pagingParamRequest)
            val endOfPaginationReached = albums.isEmpty() || albums.size < state.config.pageSize
            doTransaction(loadType, page, endOfPaginationReached, albums.toModels().toEntities())
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            currentPosition = -1
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            currentPosition = -1
            MediatorResult.Error(e)
        }
    }

    private suspend fun doTransaction(
        loadType: LoadType,
        page: Int,
        endOfPaginationReached: Boolean,
        albumModels: List<AlbumEntity>
    ) {
        database.withTransaction {
            if (loadType == LoadType.REFRESH) {
                albumDao.clearAll()
                albumRemoteKeysDao.clearAll()
            }
            val prevKey = if (page > 0) page - 1 else null
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = albumModels.map { album ->
                AlbumRemoteKeysEntity(album.id, prevKey, nextKey)
            }
            albumDao.insertAll(albumModels)
            albumRemoteKeysDao.insertAll(keys)
            dbTrackingDao.insert(DBTrackingEntity(lastAlbumUpdated = System.currentTimeMillis()))
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, AlbumListItem>
    ): AlbumRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { albumId ->
                albumRemoteKeysDao.remoteKeyAlbumId(albumId)
            }
        }
    }

    private suspend fun getRemoteKeysForLastItem(
        state: PagingState<Int, AlbumListItem>
    ): AlbumRemoteKeysEntity? {
        return state.pages.lastOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { album ->
            albumRemoteKeysDao.remoteKeyAlbumId(album.id)
        }
    }
}