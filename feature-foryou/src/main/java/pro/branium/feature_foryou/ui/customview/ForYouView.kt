package pro.branium.feature_foryou.ui.customview

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
import pro.branium.core_navigation.discovery.ForYouNavigator
import pro.branium.core_playback.PlaybackController
import pro.branium.core_resources.R
import pro.branium.core_ui.adapter.SongAdapter
import pro.branium.core_ui.databinding.LayoutEmptyListBinding
import pro.branium.core_ui.extensions.showSnackBar
import pro.branium.core_ui.layout.MaxHeightConstraintLayout
import pro.branium.core_ui.mapper.toModel
import pro.branium.feature_foryou.databinding.ViewForYouBinding
import pro.branium.feature_foryou.viewmodel.ForYouViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ForYouView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaxHeightConstraintLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var moreForYouNavigator: ForYouNavigator

    @Inject
    lateinit var playbackController: PlaybackController

    private val binding = ViewForYouBinding
        .inflate(LayoutInflater.from(context), this, true)

    private val viewModel: ForYouViewModel by lazy {
        val owner = findViewTreeViewModelStoreOwner()!!
        ViewModelProvider(owner)[ForYouViewModel::class.java]
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
            val fragmentActivity = context as FragmentActivity
            playbackController.showOptionMenu(
                true,
                { showNetworkError(this, this) },
                fragmentActivity.supportFragmentManager,
                songModel.toModel()
            )
        }
    )

    init {
        binding.includeForYouSong.rvSongList.adapter = songAdapter

        binding.layoutMoreForYou.setOnClickListener {
            val fragment = findFragment<Fragment>()
            moreForYouNavigator.openMoreForYou(fragment)
        }

        LayoutEmptyListBinding.inflate(
            LayoutInflater.from(context),
            binding.forYouEmptyLayout,
            true
        ).apply {
            iconRes = pro.branium.feature_foryou.R.drawable.ic_music_off
            message = context.getString(R.string.no_for_you)
        }

        doOnAttach {
            observeData()
        }
    }

    private fun observeData() {
        val owner = findViewTreeLifecycleOwner() ?: return
        owner.lifecycleScope.launch {
            owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.top15ForYouSongs.collect { songs ->
                    songAdapter.updateSongs(songs)
                    if (songs.isEmpty()) {
                        binding.forYouEmptyLayout.isVisible = true
                        binding.includeForYouSong.root.isVisible = false
                    } else {
                        binding.forYouEmptyLayout.isVisible = false
                        binding.includeForYouSong.root.isVisible = true
                    }
                }
            }
        }
    }

    private fun showNetworkError(root: View, anchor: View) {
        root.showSnackBar(
            R.string.no_internet,
            anchor
        )
    }
}
