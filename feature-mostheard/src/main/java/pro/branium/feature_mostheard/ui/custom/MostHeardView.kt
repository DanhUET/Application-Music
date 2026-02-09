package pro.branium.feature_mostheard.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.doOnAttach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_navigation.discovery.MostListenedNavigator
import pro.branium.core_playback.PlaybackController
import pro.branium.core_resources.R
import pro.branium.core_ui.adapter.SongAdapter
import pro.branium.core_ui.databinding.LayoutEmptyListBinding
import pro.branium.core_ui.extensions.showSnackBar
import pro.branium.core_ui.layout.MaxHeightConstraintLayout
import pro.branium.core_ui.mapper.toModel
import pro.branium.feature_mostheard.databinding.ViewMostHeardBinding
import pro.branium.feature_mostheard.viewmodel.MostHeardViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MostHeardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaxHeightConstraintLayout(context, attrs, defStyleAttr) {
    @Inject
    lateinit var playbackController: PlaybackController

    @Inject
    lateinit var navigator: MostListenedNavigator

    private val binding: ViewMostHeardBinding =
        ViewMostHeardBinding.inflate(LayoutInflater.from(context), this, true)

    private val viewModel: MostHeardViewModel by lazy {
        val owner = findViewTreeViewModelStoreOwner()!!
        ViewModelProvider(owner)[MostHeardViewModel::class.java]
    }

    private val songAdapter = SongAdapter(
        onSongClick = { songModel, index ->
            val playlist = viewModel.playlist
            val owner = findViewTreeLifecycleOwner() ?: return@SongAdapter
            playbackController.prepareToPlay(
                owner,
                true,
                { showNetworkError(this, this) },
                songModel,
                playlist,
                index
            )
        },
        onSongOptionMenuClick = { songModel ->
            playbackController.showOptionMenu(
                true,
                { showNetworkError(this, this) },
                (context as FragmentActivity).supportFragmentManager,
                songModel.toModel()
            )
        }
    )

    init {
        binding.recyclerSongs.adapter = songAdapter
        binding.layoutMoreMostHeard.setOnClickListener {
            val fragment = try {
                findFragment<Fragment>()
            } catch (_: IllegalStateException) {
                null
            }
            fragment?.let {
                navigator.openMoreMostListen(it)
            }
        }

        LayoutEmptyListBinding.inflate(
            LayoutInflater.from(context),
            binding.mostHeardEmptyLayout,
            true
        ).apply {
            iconRes = pro.branium.feature_mostheard.R.drawable.ic_headset_off
            message = context.getString(R.string.no_most_heard)
        }

        doOnAttach {
            observeData()
        }
    }

    private fun showNetworkError(rootView: View, anchorView: View) {
        rootView.showSnackBar(
            R.string.no_internet,
            anchorView
        )
    }

    private fun observeData() {
        findViewTreeLifecycleOwner()?.let { owner ->
            owner.lifecycleScope.launch {
                owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.top15MostHeardSongs.collect { songs ->
                        songAdapter.updateSongs(songs)
                        binding.mostHeardEmptyLayout.isVisible = songs.isEmpty()
                        binding.recyclerSongs.isVisible = songs.isNotEmpty()
                    }
                }
            }
        }
    }
}
