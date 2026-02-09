package pro.branium.core_playback.usecase

import pro.branium.core_domain.repository.LastPlaybackStateRepository
import javax.inject.Inject

class GetPlaybackStateUseCase @Inject constructor(
    private val repository: LastPlaybackStateRepository
) {
    operator fun invoke() = repository.loadLastPlaybackState()
}