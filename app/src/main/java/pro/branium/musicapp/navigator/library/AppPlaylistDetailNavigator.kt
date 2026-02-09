package pro.branium.musicapp.navigator.library

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pro.branium.core_navigation.library.PlaylistDetailNavigator
import pro.branium.feature_playlist.ui.fragment.PlaylistDetailFragmentArgs
import pro.branium.musicapp.R
import javax.inject.Inject

class AppPlaylistDetailNavigator @Inject constructor() : PlaylistDetailNavigator {
    override fun openPlaylistDetail(from: Fragment, playlistId: Long) {
        val navController = from.requireActivity()
            .findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(
            R.id.action_global_fr_playlist_detail,
            PlaylistDetailFragmentArgs(playlistId).toBundle()
        )
    }
}