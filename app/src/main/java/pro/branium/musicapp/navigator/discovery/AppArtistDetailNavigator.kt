package pro.branium.musicapp.navigator.discovery

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pro.branium.core_navigation.discovery.ArtistDetailNavigator
import pro.branium.feature_artist.ui.fragment.ArtistDetailFragmentArgs
import pro.branium.musicapp.R
import javax.inject.Inject

class AppArtistDetailNavigator @Inject constructor() : ArtistDetailNavigator {
    override fun openArtistDetail(from: Fragment, artistId: Int) {
        val navController = from.requireActivity()
            .findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(
            R.id.action_global_fr_artist_detail,
            ArtistDetailFragmentArgs(artistId).toBundle()
        )
    }
}