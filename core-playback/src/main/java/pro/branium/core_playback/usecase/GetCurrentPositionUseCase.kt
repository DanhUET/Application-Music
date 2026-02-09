package pro.branium.core_playback.usecase

import pro.branium.core_domain.repository.PlaybackStateRepository
import javax.inject.Inject

class  GetCurrentPositionUseCase @Inject constructor(
    private val repository: PlaybackStateRepository
) {
    operator fun invoke() = repository.currentPosition
}