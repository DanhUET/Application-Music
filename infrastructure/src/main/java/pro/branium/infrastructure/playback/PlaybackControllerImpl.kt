package pro.branium.infrastructure.playback

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pro.branium.core_domain.usecase.AskNotificationPermissionUseCase
import pro.branium.core_domain.usecase.ObservePermissionStateUseCase
import pro.branium.core_model.PlaylistModel
import pro.branium.core_model.SongModel
import pro.branium.core_navigation.navigator.SongOptionMenuNavigator
import pro.branium.core_playback.MediaPlaybackController
import pro.branium.core_playback.PlaybackController
import pro.branium.core_playback.usecase.GetCurrentPlaylistUseCase
import pro.branium.core_playback.usecase.GetPlayingSongUseCase
import pro.branium.core_playback.usecase.SetCurrentPlaylistUseCase
import javax.inject.Inject

class PlaybackControllerImpl @Inject constructor(
    private val songOptionMenuNavigator: SongOptionMenuNavigator,
    private val askPermissionUseCase: AskNotificationPermissionUseCase,
    private val observePermissionStateUseCase: ObservePermissionStateUseCase,
    private val mediaPlaybackController: MediaPlaybackController,
    private val setCurrentPlaylistUseCase: SetCurrentPlaylistUseCase,
    private val getCurrentPlaylistUseCase: GetCurrentPlaylistUseCase,
    private val getPlayingSongUseCase: GetPlayingSongUseCase
) : PlaybackController {
    override fun prepareToPlay(
        owner: LifecycleOwner,
        networkAvailable: Boolean,
        onShowNetworkError: () -> Unit,
        songModel: SongModel,
        playlist: PlaylistModel?,
        index: Int
    ) {
        val currentPlaylist = getCurrentPlaylistUseCase().value
        playlist?.let {
            if (currentPlaylist?.playlistId != it.playlistId) { // nếu khác playlist
                owner.lifecycleScope.launch {
                    setCurrentPlaylistUseCase(it, index)
                }
            }
        }
        if (networkAvailable) { // nếu có kết nối mạng
            val playingSong = getPlayingSongUseCase().value
            if (playingSong?.id != songModel.id) { // và bài hát được chọn khác với bài hát đang phát
                playSong(owner, songModel)
            }
        } else {
            onShowNetworkError()
        }
    }

    private fun playSong(
        owner: LifecycleOwner,
        songModel: SongModel
    ) {
        val permissionState = observePermissionStateUseCase().value
        val isGranted = permissionState.granted
        if (isGranted) {
            owner.lifecycleScope.launch {
                play(songModel)
            }
        } else {
            askPermissionUseCase()
        }
    }

    override fun showOptionMenu(
        networkAvailable: Boolean,
        onShowNetworkError: () -> Unit,
        fragmentManager: FragmentManager,
        songModel: SongModel
    ) {
        if (networkAvailable) {
            songOptionMenuNavigator.openSongOptionMenu(fragmentManager, songModel)
        } else {
            onShowNetworkError()
        }
    }

    override suspend fun play(song: SongModel?) {
        mediaPlaybackController.play(song?.id)
    }
}