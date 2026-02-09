package pro.branium.feature.player.ui.base

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_model.SongModel
import pro.branium.core_navigation.navigator.SongOptionMenuNavigator
import pro.branium.core_ui.mapper.toModel
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.feature.player.viewmodel.PlaybackStateViewModel
import pro.branium.feature.player.viewmodel.PlayerViewModel
import pro.branium.presentation_common.base.NetworkBaseFragment
import pro.branium.presentation_common.viewmodel.PostNotificationPermissionViewModel
import javax.inject.Inject

@AndroidEntryPoint
open class MediaPlayerBaseFragment : NetworkBaseFragment() {
    @Inject
    lateinit var songOptionMenuNavigator: SongOptionMenuNavigator
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private val postNotificationViewModel: PostNotificationPermissionViewModel by activityViewModels()
    protected val playbackStateViewModel: PlaybackStateViewModel by activityViewModels()

    protected fun prepareToPlay(view: View, songModel: DisplaySongModel) {
        val networkState = networkViewModel.isNetworkAvailable.value
        if (networkState == true) {
            playSong(songModel)
        } else {
            showNetworkError(view)
        }
    }

    protected fun playSong(songModel: DisplaySongModel) {
        val permissionState = postNotificationViewModel.state.value
        val isGranted = permissionState.granted
        if (!isGranted) {
            postNotificationViewModel.askPermission()
        } else if (permissionState.asked) {
            play(songModel)
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    postNotificationViewModel.state.collect {
                        if (it.granted) {
                            playerViewModel.onSongStarted()
                            play(songModel)
                        }
                    }
                }
            }
        }
    }

    private fun play(
        songModel: DisplaySongModel
    ) {
        playbackStateViewModel.updatePlayingSong(songModel)
    }

    protected fun showOptionMenu(songModel: DisplaySongModel, view: View) {
        val networkState = networkViewModel.isNetworkAvailable.value
        if (networkState == true) {
            showOptionMenu(songModel.toModel())
        } else {
            showNetworkError(view)
        }
    }

    private fun showOptionMenu(songModel: SongModel) {
        songOptionMenuNavigator.openSongOptionMenu(parentFragmentManager, songModel)
    }
}