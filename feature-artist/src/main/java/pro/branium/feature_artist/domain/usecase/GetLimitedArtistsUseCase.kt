package pro.branium.feature_artist.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.ArtistModel
import pro.branium.feature_artist.domain.repository.ArtistRepository
import javax.inject.Inject

class GetLimitedArtistsUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
) {
    operator fun invoke(limit: Int): Flow<PagingData<ArtistModel>> =
        artistRepository.getLimitedArtist(limit)
}