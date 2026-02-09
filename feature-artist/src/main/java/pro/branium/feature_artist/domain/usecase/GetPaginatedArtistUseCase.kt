package pro.branium.feature_artist.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.ArtistModel
import pro.branium.feature_artist.domain.repository.ArtistRepository
import javax.inject.Inject

class GetPaginatedArtistUseCase @Inject constructor(private val repository: ArtistRepository) {
    operator fun invoke(): Flow<PagingData<ArtistModel>> = repository.artistModelPagingSource
}