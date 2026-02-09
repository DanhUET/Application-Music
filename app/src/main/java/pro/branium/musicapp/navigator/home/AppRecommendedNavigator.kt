package pro.branium.musicapp.navigator.home

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pro.branium.core_navigation.home.RecommendedNavigator
import pro.branium.feature.home.ui.fragments.HomeFragmentDirections
import pro.branium.musicapp.R
import javax.inject.Inject

class AppRecommendedNavigator @Inject constructor() : RecommendedNavigator {
    override fun openMoreRecommended(from: Fragment) {
        val navController = from.requireActivity()
            .findNavController(R.id.nav_host_fragment_activity_main)
        val action = HomeFragmentDirections.actionFragmentHomeToFragmentMoreRecommended()
//        navController.navigate(R.id.fragment_more_recommended)
        navController.navigate(action)
    }
}