package pro.branium.infrastructure.repository.song

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.branium.core_model.SongModel
import pro.branium.core_domain.repository.SongRepository
import pro.branium.core_utils.MusicAppUtils.RECOMMENDED_PAGE_SIZE
import pro.branium.feature_artist.data.datasource.ArtistSongCrossRefDataSource
import pro.branium.feature_artist.data.entity.ArtistSongCrossRefEntity
import pro.branium.feature_song.data.datasource.SongLocalDataSource
import pro.branium.feature_song.data.datasource.SongRemoteDataSource
import pro.branium.infrastructure.mapper.local.songEntitiesToModels
import pro.branium.infrastructure.mapper.local.toEntities
import pro.branium.infrastructure.mapper.local.toEntity
import pro.branium.infrastructure.mapper.local.toModel
import pro.branium.infrastructure.mapper.remote.toModel
import pro.branium.infrastructure.mapper.remote.toModels
import pro.branium.infrastructure.source.mediator.SongRemoteMediator
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val localDataSource: SongLocalDataSource,
    private val remoteDataSource: SongRemoteDataSource,
    private val artistSongCrossRefDataSource: ArtistSongCrossRefDataSource,
    private val songRemoteMediator: SongRemoteMediator
) : SongRepository {
    override fun getMostHeardSongs(limit: Int): Flow<List<SongModel>> {
        return localDataSource.getMostHeardSongs(limit).map { songEntities ->
            songEntities.songEntitiesToModels()
        }
    }

    override suspend fun getForYouSongs(limit: Int): List<SongModel> {
        // todo
        return emptyList()
    }

    override suspend fun insert(vararg songModel: SongModel) {
        val songs = songModel.map {
            it.toEntity()
        }
        localDataSource.insert(*songs.toTypedArray())
    }

    override suspend fun updateSongCounter(songId: String) {
        localDataSource.updateSongCounter(songId)
    }

    @OptIn(ExperimentalPagingApi::class)
    override val songModelPagingSource: Flow<PagingData<SongModel>>
        get() = Pager(
            PagingConfig(RECOMMENDED_PAGE_SIZE),
            remoteMediator = songRemoteMediator,
        ) {
            localDataSource.songPagingSource()
        }.flow
            .map { pagingData -> pagingData.map { entity -> entity.toModel() } }

    override suspend fun getSongsInAlbum(album: String): List<SongModel> {
        return localDataSource.getSongsInAlbum(album).songEntitiesToModels()
    }

    override suspend fun getSongsInPlaylist(playlistId: Long): List<SongModel> {
        val localSongs = localDataSource.getSongsInPlaylist(playlistId)
        if (localSongs.isEmpty()) {
            return try {
                val remoteSongs = remoteDataSource.loadFirstNSongs(playlistId, 20)
                insert(*remoteSongs.toModels().toTypedArray())
                remoteSongs.toModels()
            } catch (_: Exception) {
                emptyList()
            }
        }
        return localSongs.songEntitiesToModels()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getLimitedSongsPaging(limit: Int): Flow<PagingData<SongModel>> {
        return Pager(
            PagingConfig(limit),
            remoteMediator = songRemoteMediator
        ) {
            localDataSource.getNSongPagingSource(limit)
        }.flow
            .map { pagingData -> pagingData.map { entity -> entity.toModel() } }
    }

    override suspend fun getSongById(songId: String, playlistId: Long?): SongModel? {
        val localSong = localDataSource.getSongById(songId)
        return localSong?.toModel() ?: try {
            remoteDataSource.getSongById(songId, playlistId)?.toModel()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getSongsByArtist(artistId: Int): List<SongModel> {
        val localSongs = localDataSource.getSongsByArtist(artistId)
        return try {
            val remoteSongsResponse = remoteDataSource.getSongsByArtist(artistId)
            val songsFromRemote = remoteSongsResponse.songListDto.toModels()
            if (localSongs.isEmpty() || localSongs.size != songsFromRemote.size) {
                localDataSource.insert(*songsFromRemote.toEntities().toTypedArray())
                val crossRefs = songsFromRemote.map { song ->
                    ArtistSongCrossRefEntity(song.id, artistId)
                }
                artistSongCrossRefDataSource.insertCrossRef(*crossRefs.toTypedArray())
                songsFromRemote
            } else {
                localSongs.songEntitiesToModels()
            }
        } catch (_: Exception) {
            // Log the exception for debugging
            localSongs.songEntitiesToModels()
        }
    }
}
