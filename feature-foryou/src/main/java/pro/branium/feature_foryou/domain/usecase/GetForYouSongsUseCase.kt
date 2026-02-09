package pro.branium.feature_foryou.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.SongModel
import pro.branium.core_domain.repository.RecentSongRepository
import javax.inject.Inject

class GetForYouSongsUseCase @Inject constructor(private val repository: RecentSongRepository) {
    operator fun invoke(limit: Int): Flow<List<SongModel>> =
        repository.getMostListenedSongIdsForUser(limit)
}