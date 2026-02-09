package pro.branium.infrastructure.repository.artist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import pro.branium.core_utils.MusicAppUtils.ARTIST_PAGE_SIZE
import pro.branium.core_model.ArtistModel
import pro.branium.feature_artist.data.datasource.ArtistLocalDataSource
import pro.branium.feature_artist.data.datasource.ArtistRemoteDataSource
import pro.branium.feature_artist.data.entity.ArtistEntity
import pro.branium.feature_artist.domain.repository.ArtistRepository
import pro.branium.infrastructure.mapper.local.toEntity
import pro.branium.infrastructure.mapper.local.toModel
import pro.branium.infrastructure.mapper.local.toModels
import pro.branium.infrastructure.mapper.remote.toModel
import pro.branium.infrastructure.mapper.remote.toModels
import pro.branium.infrastructure.source.mediator.ArtistRemoteMediator
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ArtistRepositoryImpl @Inject constructor(
    private val localDataSource: ArtistLocalDataSource,
    private val remoteDataSource: ArtistRemoteDataSource,
    private val artistRemoteMediator: ArtistRemoteMediator
) : ArtistRepository {
    override val artistModelPagingSource: Flow<PagingData<ArtistModel>>
        get() = Pager(
            PagingConfig(ARTIST_PAGE_SIZE),
            remoteMediator = artistRemoteMediator
        ) {
            localDataSource.artistPagingSource()
        }.flow
            .map { pagingData -> pagingData.map { it.toModel() } }

    override fun getLimitedArtist(limit: Int): Flow<PagingData<ArtistModel>> = Pager(
        PagingConfig(limit),
        remoteMediator = artistRemoteMediator
    ) {
        localDataSource.getNArtistPagingSource(limit)
    }.flow
        .map { pagingData -> pagingData.map { it.toModel() } }

    override val top15Artists: Flow<List<ArtistModel>>
        get() = localDataSource.top15Artists
            .map { it.toModels() }

//    override suspend fun getSongsForArtist(artistId: Int): List<Song> {
//        val localSongs = localDataSource.getArtistWithSongs(artistId)?.songs ?: emptyList()
//        return try {
//            val remoteSongs = remoteDataSource.getSongsForArtist(artistId).songs
//            if (localSongs.isEmpty() || localSongs.size < remoteSongs.size) {
//                val crossRefs = remoteSongs.flatMap { song ->
//                    listOf(ArtistSongCrossRef(song.id, artistId))
//                }
//                songLocalDataSource.insert(*remoteSongs.toTypedArray())
//                artistSongCrossRefDao.insertCrossRef(*crossRefs.toTypedArray())
//                remoteSongs
//            } else {
//                localSongs
//            }
//        } catch (_: Exception) {
//            localSongs
//        }
//    }

    override suspend fun getArtistById(id: Int): Flow<ArtistModel?> {
        return localDataSource.getArtistById(id).map { it?.toModel() }
    }

//    override suspend fun getArtistByName(name: String): Flow<Artist?> {
//        val localArtist = localDataSource.getArtistByName(name)
//        return if (localArtist != null) {
//            flowOf(localArtist)
//        } else {
//            try {
//                val remoteArtist = remoteDataSource.getArtistByName(name)
//                flowOf(remoteArtist)
//            } catch (_: Exception) {
//                flowOf(null)
//            }
//        }
//    }

    override suspend fun getArtistByName(name: String): Flow<ArtistModel?> = flow {
        val localArtist = localDataSource.getArtistByName(name)
        if (localArtist != null) {
            emit(localArtist.toModel())
        } else {
            try {
                val remoteArtist = remoteDataSource.getArtistByName(name)
                emit(remoteArtist?.toModel())
            } catch (_: Exception) {
                emit(null)
            }
        }
    }

    override suspend fun insert(vararg artistModels: ArtistModel) {
        val artists = artistModels.map { it.toEntity() }.toTypedArray<ArtistEntity>()
        localDataSource.insert(*artists)
    }

    override suspend fun update(artistModel: ArtistModel) {
        localDataSource.update(artistModel.toEntity())
    }

    override suspend fun delete(artistModel: ArtistModel) {
        localDataSource.delete(artistModel.toEntity())
    }

    override suspend fun clearAll() {
        localDataSource.clearAll()
    }

    override suspend fun loadArtistPaging(limit: Int, offset: Int): List<ArtistModel> {
        return try {
            remoteDataSource.loadArtistPaging(limit, offset).toModels()
        } catch (_: Exception) {
            emptyList()
        }
    }
}