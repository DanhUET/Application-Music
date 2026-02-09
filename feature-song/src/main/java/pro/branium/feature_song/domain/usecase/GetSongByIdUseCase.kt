package pro.branium.feature_song.domain.usecase

import pro.branium.core_domain.repository.SongRepository
import pro.branium.core_model.SongModel
import javax.inject.Inject

class GetSongByIdUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(songId: String): SongModel? {
        return songRepository.getSongById(songId)
    }
}