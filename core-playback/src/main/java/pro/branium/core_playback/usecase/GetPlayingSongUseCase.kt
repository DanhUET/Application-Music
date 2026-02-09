package pro.branium.core_playback.usecase

import pro.branium.core_domain.repository.PlaybackStateRepository
import javax.inject.Inject

class GetPlayingSongUseCase @Inject constructor(
    private val playbackStateRepository: PlaybackStateRepository
) {
    operator fun invoke() = playbackStateRepository.nowPlaying
}