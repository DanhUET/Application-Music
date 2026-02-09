package pro.branium.core_playback

import androidx.media3.session.MediaController
import kotlinx.coroutines.flow.StateFlow

interface MediaControllerProvider {
    val controllerFlow: StateFlow<MediaController?>
    suspend fun await(): MediaController
}