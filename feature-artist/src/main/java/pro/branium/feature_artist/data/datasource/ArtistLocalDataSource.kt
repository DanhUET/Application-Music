package pro.branium.feature_artist.data.datasource

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import pro.branium.feature_artist.data.entity.ArtistEntity

interface ArtistLocalDataSource {
    val artists: Flow<List<ArtistEntity>>

    fun artistPagingSource(): PagingSource<Int, ArtistEntity>

    fun getNArtistPagingSource(limit: Int): PagingSource<Int, ArtistEntity>

    val top15Artists: Flow<List<ArtistEntity>>

    suspend fun getArtistById(id: Int): Flow<ArtistEntity?>

    suspend fun getArtistByName(name: String): ArtistEntity?

    suspend fun insert(vararg artists: ArtistEntity)

    suspend fun delete(artist: ArtistEntity)

    suspend fun clearAll()

    suspend fun update(artist: ArtistEntity)
}