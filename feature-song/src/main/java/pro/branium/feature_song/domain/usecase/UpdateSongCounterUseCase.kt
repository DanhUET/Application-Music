package pro.branium.feature_song.domain.usecase

import pro.branium.core_domain.repository.SongRepository
import javax.inject.Inject

class UpdateSongCounterUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(songId: String) {
        songRepository.updateSongCounter(songId)
    }
}