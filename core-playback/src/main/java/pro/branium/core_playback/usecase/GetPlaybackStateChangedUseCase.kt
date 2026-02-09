package pro.branium.core_playback.usecase

import pro.branium.core_domain.repository.PlaybackStateRepository
import javax.inject.Inject

class GetPlaybackStateChangedUseCase @Inject constructor(
    private val playbackStateRepository: PlaybackStateRepository
) {
    operator fun invoke() = playbackStateRepository.playbackStateChanged
}