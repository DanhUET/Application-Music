package pro.branium.feature.player.domain.usecase

import pro.branium.core_domain.repository.SongRepository
import pro.branium.core_model.SongModel
import javax.inject.Inject

class GetSongByIdUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(songId: String): SongModel? {
        return repository.getSongById(songId)
    }
}