package pro.branium.feature_song.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pro.branium.core_domain.repository.SongRepository
import pro.branium.core_model.SongModel
import javax.inject.Inject

class GetLimitedSongsUseCase @Inject constructor(
    private val repository: SongRepository
) {
    operator fun invoke(limit: Int): Flow<PagingData<SongModel>> {
        return repository.getLimitedSongsPaging(limit)
    }
}