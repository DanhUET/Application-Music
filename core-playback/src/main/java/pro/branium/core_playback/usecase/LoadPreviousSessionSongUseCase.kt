package pro.branium.core_playback.usecase

import pro.branium.core_model.SongModel
import pro.branium.core_domain.repository.SongRepository
import javax.inject.Inject

class LoadPreviousSessionSongUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(songId: String, playlistId: Long?): SongModel? {
        return repository.getSongById(songId, playlistId)
    }
}