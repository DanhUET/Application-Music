package pro.branium.musicapp.navigator.discovery

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pro.branium.core_navigation.discovery.ArtistNavigator
import pro.branium.discovery.ui.fragment.DiscoveryFragmentDirections
import pro.branium.musicapp.R
import javax.inject.Inject

class AppArtistNavigator @Inject constructor() : ArtistNavigator {
    override fun openMoreArtist(from: Fragment) {
        val navController = from.requireActivity()
            .findNavController(R.id.nav_host_fragment_activity_main)
        val action = DiscoveryFragmentDirections.actionFrDiscoveryToMoreArtist()
        navController.navigate(action)
    }
}