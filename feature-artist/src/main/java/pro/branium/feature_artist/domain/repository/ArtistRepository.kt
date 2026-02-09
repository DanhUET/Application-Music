package pro.branium.feature_artist.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.ArtistModel

interface ArtistRepository {
    val artistModelPagingSource: Flow<PagingData<ArtistModel>>

    fun getLimitedArtist(limit: Int): Flow<PagingData<ArtistModel>>

    val top15Artists: Flow<List<ArtistModel>>

    suspend fun getArtistById(id: Int): Flow<ArtistModel?>

    suspend fun getArtistByName(name: String): Flow<ArtistModel?>

    suspend fun insert(vararg artistModels: ArtistModel)

    suspend fun update(artistModel: ArtistModel)

    suspend fun clearAll()

    suspend fun delete(artistModel: ArtistModel)

    suspend fun loadArtistPaging(limit: Int, offset: Int): List<ArtistModel>
}