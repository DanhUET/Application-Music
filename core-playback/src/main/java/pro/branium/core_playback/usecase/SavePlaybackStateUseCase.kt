package pro.branium.core_playback.usecase

import pro.branium.core_domain.repository.LastPlaybackStateRepository
import pro.branium.core_model.PlaybackStateData
import javax.inject.Inject

class SavePlaybackStateUseCase @Inject constructor(
    private val repository: LastPlaybackStateRepository
) {
    operator fun invoke(state: PlaybackStateData) = repository.saveLastPlaybackState(state)
}