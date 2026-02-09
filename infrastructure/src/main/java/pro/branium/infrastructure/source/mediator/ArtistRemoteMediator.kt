package pro.branium.infrastructure.source.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pro.branium.core_network.dto.paging_param.PagingParamRequest
import pro.branium.core_database.entity.paging.ArtistRemoteKeysEntity
import pro.branium.core_utils.MusicAppUtils.CACHE_TIMEOUT
import pro.branium.feature_artist.data.datasource.ArtistRemoteDataSource
import pro.branium.feature_artist.data.entity.ArtistEntity
import pro.branium.infrastructure.db.AppDatabase
import pro.branium.infrastructure.entity.tracking.DBTrackingEntity
import pro.branium.infrastructure.mapper.local.toEntities
import pro.branium.infrastructure.mapper.remote.toModels
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ArtistRemoteMediator @Inject constructor(
    private val remoteDataSource: ArtistRemoteDataSource,
    private val database: AppDatabase
) : RemoteMediator<Int, ArtistEntity>() {
    private val artistDao = database.artistDao()
    private val artistRemoteKeysDao = database.artistRemoteKeysDao()
    private val dbTrackingDao = database.dbTrackingDao()
    private var currentPosition = -1

    override suspend fun initialize(): InitializeAction {
        return withContext(Dispatchers.IO) {
            val lastArtistUpdated = dbTrackingDao.artistTracking?.lastArtistUpdated ?: 0
            val cacheTimeout = TimeUnit.MILLISECONDS.convert(CACHE_TIMEOUT, TimeUnit.HOURS)
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastArtistUpdated < cacheTimeout) {
                InitializeAction.SKIP_INITIAL_REFRESH
            } else {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArtistEntity>
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
            val artists = remoteDataSource.loadArtistPaging(
                pagingParamRequest.limit,
                pagingParamRequest.offset
            )
            val endOfPaginationReached = artists.isEmpty() || artists.size < state.config.pageSize
            doTransaction(loadType, page, endOfPaginationReached, artists.toModels().toEntities())
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
        artistModels: List<ArtistEntity>
    ) {
        database.withTransaction {
            if (loadType == LoadType.REFRESH) {
                artistDao.clearAll()
                artistRemoteKeysDao.clearAll()
            }
            val prevKey = if (page > 0) page - 1 else null
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = artistModels.map { artist ->
                ArtistRemoteKeysEntity(artist.id, prevKey, nextKey)
            }
            artistDao.insert(*artistModels.toTypedArray())
            artistRemoteKeysDao.insertAll(keys)
            dbTrackingDao.insert(DBTrackingEntity(lastArtistUpdated = System.currentTimeMillis()))
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ArtistEntity>
    ): ArtistRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { artistId ->
                artistRemoteKeysDao.remoteKeysArtistId(artistId)
            }
        }
    }

    private suspend fun getRemoteKeysForLastItem(
        state: PagingState<Int, ArtistEntity>
    ): ArtistRemoteKeysEntity? {
        return state.pages.lastOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { artist ->
            artistRemoteKeysDao.remoteKeysArtistId(artist.id)
        }
    }
}