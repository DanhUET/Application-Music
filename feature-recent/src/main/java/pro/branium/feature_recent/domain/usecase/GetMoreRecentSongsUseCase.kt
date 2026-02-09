package pro.branium.feature_recent.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.SongModel
import pro.branium.core_domain.repository.RecentSongRepository
import javax.inject.Inject

class GetMoreRecentSongsUseCase @Inject constructor(
    private val repository: RecentSongRepository
) {
    operator fun invoke(): Flow<List<SongModel>> = repository.getMoreRecentSongs()
}