package pro.branium.musicapp.navigator.library

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pro.branium.core_navigation.library.RecentNavigator
import pro.branium.feature.library.ui.fragments.LibraryFragmentDirections
import pro.branium.musicapp.R
import javax.inject.Inject

class AppRecentNavigator @Inject constructor() : RecentNavigator {
    override fun openMoreRecent(from: Fragment) {
        val navController = from.requireActivity()
            .findNavController(R.id.nav_host_fragment_activity_main)
        val action = LibraryFragmentDirections.actionFrLibraryToMoreRecent()
        navController.navigate(action)
    }
}