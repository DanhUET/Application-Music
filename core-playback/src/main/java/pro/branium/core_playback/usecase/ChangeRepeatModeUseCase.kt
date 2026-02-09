package pro.branium.core_playback.usecase

import pro.branium.core_playback.MediaPlaybackController
import javax.inject.Inject

class ChangeRepeatModeUseCase @Inject constructor(
    private val controller: MediaPlaybackController
) {
    operator fun invoke() = controller.changeRepeatMode()
}