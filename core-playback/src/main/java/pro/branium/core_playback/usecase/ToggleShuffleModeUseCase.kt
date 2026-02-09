package pro.branium.core_playback.usecase

import pro.branium.core_playback.MediaPlaybackController
import javax.inject.Inject

class ToggleShuffleModeUseCase @Inject constructor(
    private val repository: MediaPlaybackController
) {
    operator fun invoke() = repository.toggleShuffleMode()
}