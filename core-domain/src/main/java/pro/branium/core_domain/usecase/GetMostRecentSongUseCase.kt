package pro.branium.core_domain.usecase

import pro.branium.core_domain.repository.RecentSongRepository
import javax.inject.Inject

class GetMostRecentSongUseCase @Inject constructor(
    private val recentSongRepository: RecentSongRepository
) {
    operator fun invoke() = recentSongRepository.getMostRecentSong()
}