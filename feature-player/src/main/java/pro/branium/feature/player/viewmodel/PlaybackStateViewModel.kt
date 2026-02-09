package pro.branium.feature.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.branium.core_playback.MediaPlaybackController
import pro.branium.core_domain.repository.RepeatMode
import pro.branium.core_playback.usecase.GetCurrentPositionUseCase
import pro.branium.core_playback.usecase.GetPlaybackStateChangedUseCase
import pro.branium.core_playback.usecase.GetPlayingSongUseCase
import pro.branium.core_playback.usecase.GetRepeatModeUseCase
import pro.branium.core_playback.usecase.GetShuffleModeUseCase
import pro.branium.core_model.NowPlaying
import pro.branium.core_ui.mapper.toDisplayModel
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.feature.player.domain.usecase.GetSongByIdUseCase
import javax.inject.Inject

@HiltViewModel
class PlaybackStateViewModel @Inject constructor(
    private val mediaPlaybackController: MediaPlaybackController,
    private val getSongByIdUseCase: GetSongByIdUseCase,
    getPlayingSongUseCase: GetPlayingSongUseCase,
    getPlaybackStateChangedUseCase: GetPlaybackStateChangedUseCase,
    getShuffleModeUseCase: GetShuffleModeUseCase,
    getRepeatModeUseCase: GetRepeatModeUseCase,
    getCurrentPositionUseCase: GetCurrentPositionUseCase
) : ViewModel() {
    private val _nowPlayingSong = MutableStateFlow<DisplaySongModel?>(null)
    private val _currentAngle = MutableStateFlow(0f)

    val nowPlayingSong: StateFlow<DisplaySongModel?> = _nowPlayingSong
    val nowPlayingState: StateFlow<NowPlaying?> = getPlayingSongUseCase()
    val playbackStateChanged: StateFlow<Int> = getPlaybackStateChangedUseCase()

    val currentAngle: StateFlow<Float> = _currentAngle
    val shuffleMode: StateFlow<Boolean> = getShuffleModeUseCase()
    val repeatMode: StateFlow<RepeatMode> = getRepeatModeUseCase()
    val currentPosition: StateFlow<Long> = getCurrentPositionUseCase()

    fun updatePlayingSong(songModel: DisplaySongModel) {
        _nowPlayingSong.value = songModel
    }

    /**
     * Get song by given id
     *
     * @param songId id of song need to get
     */
    fun getSongById(songId: String?) {
        if (songId == null) return
        viewModelScope.launch {
            val song = getSongByIdUseCase(songId) ?: return@launch
            updatePlayingSong(song.toDisplayModel())
        }
    }

    fun updateCurrentAngle(angle: Float) {
        _currentAngle.value = angle
    }

    fun playPause() {
        if (nowPlayingState.value?.isPlaying == true) {
            pause()
        } else {
            play()
        }
    }

    fun play() {
        viewModelScope.launch {
            mediaPlaybackController.play(null)
        }
    }

    fun pause() {
        mediaPlaybackController.pause()
    }

    fun seekToNext() {
        if (mediaPlaybackController.hasNextMedia()) {
            mediaPlaybackController.seekToNext()
        }
    }

    fun seekToPrevious() {
        if (mediaPlaybackController.hasPreviousMedia()) {
            mediaPlaybackController.seekToPrevious()
        }
    }

    fun getSongIndexToPlay(songId: String?): Int {
        return mediaPlaybackController.getCurrentPlayingSongIndex()
    }

    fun hasNextMediaItem(): Boolean = mediaPlaybackController.hasNextMedia()

    fun hasPreviousMediaItem(): Boolean = mediaPlaybackController.hasPreviousMedia()

    fun toggleShuffleMode() {
        mediaPlaybackController.toggleShuffleMode()
    }

    fun changeRepeatMode() {
        mediaPlaybackController.changeRepeatMode()
    }
}