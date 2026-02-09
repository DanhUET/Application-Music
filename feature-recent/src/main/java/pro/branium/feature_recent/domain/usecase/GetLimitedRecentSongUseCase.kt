package pro.branium.feature_recent.domain.usecase

import pro.branium.core_domain.repository.RecentSongRepository
import javax.inject.Inject

class GetLimitedRecentSongUseCase @Inject constructor(
    private val repository: RecentSongRepository
) {
    operator fun invoke(limit: Int) = repository.getLimitedRecentSongs(limit)
}