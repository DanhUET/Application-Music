package pro.branium.musicapp.navigator.home

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pro.branium.core_navigation.home.AlbumDetailNavigator
import pro.branium.feature_album.ui.fragment.AlbumDetailsFragmentArgs
import pro.branium.musicapp.R
import javax.inject.Inject

class AppAlbumDetailNavigator @Inject constructor() : AlbumDetailNavigator {
    override fun openAlbumDetail(from: Fragment, albumId: Int) {
        val navController = from.requireActivity()
            .findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(
            R.id.action_global_fr_album_detail,
            AlbumDetailsFragmentArgs(albumId).toBundle()
        )
    }
}