package pro.branium.musicapp.navigator.library

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pro.branium.core_navigation.library.FavoriteSongNavigator
import pro.branium.feature.library.ui.fragments.LibraryFragmentDirections
import pro.branium.musicapp.R
import javax.inject.Inject

class AppFavoriteSongNavigator @Inject constructor() : FavoriteSongNavigator {
    override fun openMoreFavoriteSong(from: Fragment) {
        val navController = from.requireActivity()
            .findNavController(R.id.nav_host_fragment_activity_main)
        val action = LibraryFragmentDirections.actionFrLibraryToMoreFavorite()
        navController.navigate(action)
    }
}