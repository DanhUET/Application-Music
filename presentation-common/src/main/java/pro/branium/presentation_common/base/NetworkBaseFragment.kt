package pro.branium.presentation_common.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_resources.R
import pro.branium.core_ui.base.SnackBarAnchorProvider
import pro.branium.presentation_common.viewmodel.NetworkStateViewModel

@AndroidEntryPoint
open class NetworkBaseFragment : Fragment() {
    protected val networkViewModel: NetworkStateViewModel by activityViewModels()

    protected fun showNetworkError(view: View) {
        val message = getString(R.string.no_internet)
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val anchorView = (requireActivity() as? SnackBarAnchorProvider)?.getSnackBarAnchorView()
        if (anchorView != null && anchorView.isShown) {
            snackBar.anchorView = anchorView
        }
        snackBar.show()
    }
}