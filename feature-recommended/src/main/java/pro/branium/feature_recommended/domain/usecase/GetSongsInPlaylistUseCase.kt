package pro.branium.feature_recommended.domain.usecase

import pro.branium.core_domain.repository.SongRepository
import pro.branium.core_model.SongModel
import javax.inject.Inject

class GetSongsInPlaylistUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(playlistId: Long): List<SongModel> {
        return repository.getSongsInPlaylist(playlistId)
    }
}