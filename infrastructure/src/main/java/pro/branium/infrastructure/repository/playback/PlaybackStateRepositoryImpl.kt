package pro.branium.infrastructure.repository.playback

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import pro.branium.core_model.PlaylistModel
import pro.branium.core_domain.repository.PlaybackStateRepository
import pro.branium.core_domain.repository.RepeatMode
import pro.branium.core_model.NowPlaying
import pro.branium.core_utils.ApplicationScope
import pro.branium.infrastructure.media.MediaControllerHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackStateRepositoryImpl @Inject constructor(
    private val holder: MediaControllerHolder,
    @ApplicationScope appScope: CoroutineScope
) : PlaybackStateRepository {
    private val _playbackStateChanged = MutableStateFlow(0)
    private val _currentPosition = MutableStateFlow(0L)
    private val _now = MutableStateFlow(NowPlaying(null, false))
    private val _currentPlaylist = MutableStateFlow<PlaylistModel?>(null)
    private val _shuffleMode = MutableStateFlow(false)
    private val _repeatMode = MutableStateFlow(RepeatMode.NONE)

    override val nowPlaying: StateFlow<NowPlaying>
        get() = _now.asStateFlow()

    override val currentPosition: StateFlow<Long>
        get() = _currentPosition.asStateFlow()

    override val shuffleMode: StateFlow<Boolean>
        get() = _shuffleMode.asStateFlow()

    override val repeatMode: StateFlow<RepeatMode>
        get() = _repeatMode.asStateFlow()

    override val playbackStateChanged: StateFlow<Int>
        get() = _playbackStateChanged.asStateFlow()

    override val currentPlaylist: StateFlow<PlaylistModel?>
        get() = _currentPlaylist.asStateFlow()

    init {
        appScope.launch {
            val controller = holder.await()
            fun publishNowPlaying() {
                _now.tryEmit(
                    NowPlaying(
                        id = controller.currentMediaItem?.mediaId,
                        isPlaying = controller.isPlaying,
                        durationMs = controller.duration
                    )
                )
            }

            fun publishShuffleMode() {
                _shuffleMode.tryEmit(controller.shuffleModeEnabled)
            }

            fun publishRepeatMode() {
                _repeatMode.tryEmit(
                    when (controller.repeatMode) {
                        Player.REPEAT_MODE_ALL -> RepeatMode.ALL
                        Player.REPEAT_MODE_ONE -> RepeatMode.ONE
                        else -> RepeatMode.NONE
                    }
                )
            }

            fun publishCurrentPosition() {
                _currentPosition.tryEmit(controller.currentPosition)
            }

            controller.addListener(object : Player.Listener {
                override fun onMediaItemTransition(item: MediaItem?, reason: Int) =
                    publishNowPlaying()

                override fun onIsPlayingChanged(isPlaying: Boolean) = publishNowPlaying()
                override fun onEvents(player: Player, events: Player.Events) {
                    if (events.contains(Player.EVENT_REPEAT_MODE_CHANGED)) publishRepeatMode()
                    if (events.contains(Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED))
                        publishShuffleMode()
                    if (
                        events.contains(Player.EVENT_POSITION_DISCONTINUITY) ||
                        events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)
                    ) {
                        publishCurrentPosition()
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    _playbackStateChanged.tryEmit(playbackState)
                }
            })

            launch {
                while (isActive) {
                    if (controller.isPlaying) {
                        _currentPosition.value = controller.currentPosition
                    }
                    delay(500)
                }
            }

            publishNowPlaying()
            publishShuffleMode()
            publishRepeatMode()
            publishCurrentPosition()
        }
    }

    override fun updateCurrentPlaylist(playlist: PlaylistModel?) {
        _currentPlaylist.value = playlist
    }
}
