package pro.branium.feature_artist.data.datasource

import pro.branium.feature_artist.data.entity.ArtistSongCrossRefEntity


interface ArtistSongCrossRefDataSource {
    suspend fun getSongIdsForArtist(artistId: Int): List<String>

    suspend fun insertCrossRef(vararg artistSongCrossRef: ArtistSongCrossRefEntity)

    suspend fun deleteCrossRef(vararg artistSongCrossRef: ArtistSongCrossRefEntity)

    suspend fun clearAll()
}