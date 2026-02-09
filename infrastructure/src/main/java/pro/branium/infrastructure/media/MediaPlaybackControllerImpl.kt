package pro.branium.infrastructure.media

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pro.branium.core_playback.MediaControllerProvider
import pro.branium.core_playback.MediaPlaybackController
import pro.branium.core_domain.repository.PlaybackStateRepository
import javax.inject.Inject

class MediaPlaybackControllerImpl @Inject constructor(
    private val controllerHolder: MediaControllerProvider,
    private val playbackStateRepository: PlaybackStateRepository
) : MediaPlaybackController {
    override suspend fun setPlaylist(mediaItems: List<MediaItem>, startIndex: Int) {
        val controller = controllerHolder.await()
        withContext(Dispatchers.Main) {
            controller.setMediaItems(mediaItems, startIndex, 0L)
            controller.prepare()
        }
    }

    override fun hasNextMedia(): Boolean {
        return controllerHolder.controllerFlow.value?.hasNextMediaItem() ?: false
    }

    override fun hasPreviousMedia(): Boolean {
        return controllerHolder.controllerFlow.value?.hasPreviousMediaItem() ?: false
    }

    override suspend fun play(songId: String?) {
        val controller = controllerHolder.await()
        withContext(Dispatchers.Main) {
            if (songId == null) {
                controller.play()
            } else {
                val playlist = playbackStateRepository.currentPlaylist.value ?: return@withContext
                val targetIndex = playlist.songs.indexOfFirst { it.id == songId }
                if (targetIndex != -1) {
                    controller.seekTo(targetIndex, 0)
                    controller.play()
                }
            }
        }
    }

    override fun pause() {
        controllerHolder.controllerFlow.value?.pause()
    }

    override fun seekTo(position: Long) {
        controllerHolder.controllerFlow.value?.seekTo(position)
    }

    override fun seekToNext() {
        controllerHolder.controllerFlow.value?.seekToNext()
    }

    override fun seekToPrevious() {
        controllerHolder.controllerFlow.value?.seekToPrevious()
    }

    override fun getCurrentPlayingSongId(): String? {
        return controllerHolder.controllerFlow.value?.currentMediaItem?.mediaId
    }

    override fun getCurrentPlayingSongIndex(): Int {
        return controllerHolder.controllerFlow.value?.currentMediaItemIndex ?: -1
    }

    override fun toggleShuffleMode() {
        val controller = controllerHolder.controllerFlow.value ?: return
        controller.shuffleModeEnabled = !controller.shuffleModeEnabled
    }

    override fun changeRepeatMode() {
        val controller = controllerHolder.controllerFlow.value ?: return
        val currentMode = controller.repeatMode
        val newMode = when (currentMode) {
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_OFF
        }
        controller.repeatMode = newMode
    }
}
