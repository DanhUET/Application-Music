package pro.branium.core_playback.usecase

import kotlinx.coroutines.flow.StateFlow
import pro.branium.core_domain.repository.PlaybackStateRepository
import pro.branium.core_model.PlaylistModel
import javax.inject.Inject

class GetCurrentPlaylistUseCase @Inject constructor(
    private val playbackStateRepository: PlaybackStateRepository
) {
    operator fun invoke(): StateFlow<PlaylistModel?> = playbackStateRepository.currentPlaylist
}