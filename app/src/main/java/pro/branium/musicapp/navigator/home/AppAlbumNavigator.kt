package pro.branium.musicapp.navigator.home

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pro.branium.core_navigation.home.AlbumNavigator
import pro.branium.feature.home.ui.fragments.HomeFragmentDirections
import pro.branium.musicapp.R
import javax.inject.Inject

class AppAlbumNavigator @Inject constructor() : AlbumNavigator {
    override fun openMoreAlbum(from: Fragment) {
        val navController = from.requireActivity()
            .findNavController(R.id.nav_host_fragment_activity_main)
        val action = HomeFragmentDirections.actionFragmentHomeToFragmentMoreAlbum()
        navController.navigate(action)
    }
}