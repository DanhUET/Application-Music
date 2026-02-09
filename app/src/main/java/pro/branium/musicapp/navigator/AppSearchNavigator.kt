package pro.branium.musicapp.navigator

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import pro.branium.core_navigation.navigator.SearchNavigator
import pro.branium.feature.search.ui.fragments.SearchingFragmentDirections
import pro.branium.musicapp.R
import javax.inject.Inject

class AppSearchNavigator @Inject constructor(
    private val context: Context
) : SearchNavigator {
    override fun openSearch(query: String?) {
        val action = SearchingFragmentDirections.actionGlobalFrSearching(query)
        val activity = context as? FragmentActivity ?: return
        val navController = activity.findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(action)
    }
}