package pro.branium.infrastructure.source.artist

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import pro.branium.feature_artist.data.dao.ArtistDao
import pro.branium.feature_artist.data.datasource.ArtistLocalDataSource
import pro.branium.feature_artist.data.entity.ArtistEntity
import javax.inject.Inject

class ArtistLocalDataSourceImpl @Inject constructor(
    private val artistDao: ArtistDao
) : ArtistLocalDataSource {
    override val artists: Flow<List<ArtistEntity>>
        get() = artistDao.artists

    override fun artistPagingSource(): PagingSource<Int, ArtistEntity> =
        artistDao.artistPagingSource()

    override fun getNArtistPagingSource(limit: Int): PagingSource<Int, ArtistEntity> {
        return artistDao.getNArtistPagingSource(limit)
    }

    override val top15Artists: Flow<List<ArtistEntity>>
        get() = artistDao.top15Artists

    override suspend fun getArtistById(id: Int): Flow<ArtistEntity?> {
        return artistDao.getArtistById(id)
    }

    override suspend fun getArtistByName(name: String): ArtistEntity? {
        return artistDao.getArtistByName(name)
    }

    override suspend fun insert(vararg artists: ArtistEntity) {
        artistDao.insert(*artists)
    }

    override suspend fun delete(artist: ArtistEntity) {
        artistDao.delete(artist)
    }

    override suspend fun clearAll() {
        artistDao.clearAll()
    }

    override suspend fun update(artist: ArtistEntity) {
        artistDao.update(artist)
    }
}