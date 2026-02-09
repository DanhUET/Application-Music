package pro.branium.feature_artist.data.datasource

import pro.branium.feature_artist.data.dto.ArtistDto

interface ArtistRemoteDataSource {
    suspend fun loadArtistPaging(limit: Int, offset: Int): List<ArtistDto>

    suspend fun getArtistById(artistId: Int): ArtistDto?

    suspend fun getArtistByName(artistName: String): ArtistDto?
}