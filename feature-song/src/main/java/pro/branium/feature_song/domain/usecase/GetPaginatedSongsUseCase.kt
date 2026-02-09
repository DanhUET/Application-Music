package pro.branium.feature_song.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pro.branium.core_domain.repository.SongRepository
import pro.branium.core_model.SongModel
import javax.inject.Inject

class GetPaginatedSongsUseCase @Inject constructor(
    private val repository: SongRepository
) {
    operator fun invoke(): Flow<PagingData<SongModel>> = repository.songModelPagingSource
}