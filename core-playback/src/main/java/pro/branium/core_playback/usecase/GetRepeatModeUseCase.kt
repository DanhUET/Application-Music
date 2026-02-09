package pro.branium.core_playback.usecase

import pro.branium.core_domain.repository.PlaybackStateRepository
import javax.inject.Inject

class GetRepeatModeUseCase @Inject constructor(
    private val repository: PlaybackStateRepository
) {
    operator fun invoke() = repository.repeatMode
}