package pro.branium.musicapp.navigator.discovery

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pro.branium.core_navigation.discovery.ForYouNavigator
import pro.branium.discovery.ui.fragment.DiscoveryFragmentDirections
import pro.branium.musicapp.R
import javax.inject.Inject

class AppForYouNavigator @Inject constructor() : ForYouNavigator {
    override fun openMoreForYou(from: Fragment) {
        val navController = from.requireActivity()
            .findNavController(R.id.nav_host_fragment_activity_main)
        val action = DiscoveryFragmentDirections.actionFrDiscoveryToMoreForYou()
        navController.navigate(action)
    }
}