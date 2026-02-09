package pro.branium.feature_album.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pro.branium.feature_album.domain.model.AlbumSummaryModel
import pro.branium.feature_album.domain.repository.AlbumRepository
import javax.inject.Inject

class GetLimitedAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    operator fun invoke(limit: Int = 10): Flow<PagingData<AlbumSummaryModel>> =
        repository.getNAlbumPagingSource(limit)
}