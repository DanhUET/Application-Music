package pro.branium.feature_album.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pro.branium.feature_album.domain.model.AlbumSummaryModel
import pro.branium.feature_album.domain.repository.AlbumRepository
import javax.inject.Inject

class GetPaginatedAlbumsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    operator fun invoke(): Flow<PagingData<AlbumSummaryModel>> = repository.albumPagingSource
}