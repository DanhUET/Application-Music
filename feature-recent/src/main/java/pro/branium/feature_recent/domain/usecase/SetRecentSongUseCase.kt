package pro.branium.feature_recent.domain.usecase

import pro.branium.core_domain.repository.RecentSongRepository
import javax.inject.Inject

class SetRecentSongUseCase @Inject constructor(private val repository: RecentSongRepository) {
    suspend operator fun invoke(songId: String) {
        repository.setRecentSong(songId)
    }
}