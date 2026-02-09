package pro.branium.presentation_common.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_playback.PlaybackController
import pro.branium.core_resources.R
import pro.branium.core_ui.extensions.showSnackBar
import pro.branium.presentation_common.viewmodel.NetworkStateViewModel
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {
    @Inject
    lateinit var playbackController: PlaybackController

    protected val networkViewModel: NetworkStateViewModel by activityViewModels()

    protected fun showNetworkError(rootView: View, anchorView: View) {
        rootView.showSnackBar(
            R.string.no_internet,
            anchorView
        )
    }
}