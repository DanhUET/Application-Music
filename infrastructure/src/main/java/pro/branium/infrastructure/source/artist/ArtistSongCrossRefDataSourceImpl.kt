package pro.branium.infrastructure.source.artist

import pro.branium.feature_artist.data.dao.ArtistSongCrossRefDao
import pro.branium.feature_artist.data.datasource.ArtistSongCrossRefDataSource
import pro.branium.feature_artist.data.entity.ArtistSongCrossRefEntity
import javax.inject.Inject

class ArtistSongCrossRefDataSourceImpl @Inject constructor(
    private val artistSongCrossRefDao: ArtistSongCrossRefDao
) : ArtistSongCrossRefDataSource {
    override suspend fun getSongIdsForArtist(artistId: Int): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCrossRef(vararg artistSongCrossRef: ArtistSongCrossRefEntity) {
        artistSongCrossRefDao.insertCrossRef(*artistSongCrossRef)
    }

    override suspend fun deleteCrossRef(vararg artistSongCrossRef: ArtistSongCrossRefEntity) {
        artistSongCrossRefDao.deleteCrossRef(*artistSongCrossRef)
    }

    override suspend fun clearAll() {
        artistSongCrossRefDao.clearAll()
    }
}