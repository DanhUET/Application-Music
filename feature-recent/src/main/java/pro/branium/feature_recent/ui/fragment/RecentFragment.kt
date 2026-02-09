package pro.branium.feature_recent.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_navigation.library.RecentNavigator
import pro.branium.core_ui.mapper.toModel
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.core_utils.MusicAppUtils
import pro.branium.feature_recent.databinding.FragmentRecentBinding
import pro.branium.feature_recent.ui.adapter.RecentSongAdapter
import pro.branium.feature_recent.viewmodel.RecentViewModel
import pro.branium.presentation_common.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class RecentFragment : BaseFragment() {
    @Inject
    lateinit var recentNavigator: RecentNavigator
    private lateinit var binding: FragmentRecentBinding
    private lateinit var adapter: RecentSongAdapter
    private val recentViewModel: RecentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        adapter = RecentSongAdapter(
            object : RecentSongAdapter.OnSongClickListener {
                override fun onClick(songModel: DisplaySongModel, index: Int) {
                    playbackController.prepareToPlay(
                        viewLifecycleOwner,
                        true, {
                            showNetworkError(binding.root, binding.root)
                        },
                        songModel.song,
                        recentViewModel.playlist,
                        index
                    )
                }
            },
            object : RecentSongAdapter.OnSongOptionMenuClickListener {
                override fun onClick(songModel: DisplaySongModel) {
                    val networkState = networkViewModel.isNetworkAvailable.value
                    if (networkState == true) {
                        playbackController.showOptionMenu(
                            true,
                            {
                                showNetworkError(binding.root, binding.root)
                            },
                            parentFragmentManager,
                            songModel.toModel()
                        )
                    } else {
                        showNetworkError(binding.root, binding.root)
                    }
                }
            }
        )
        val layoutManager = MyLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.HORIZONTAL,
            false
        )
        binding.rvRecent.adapter = adapter
        binding.rvRecent.layoutManager = layoutManager
        binding.progressRecentHeard.visibility = View.VISIBLE
        binding.layoutMoreRecent.setOnClickListener {
            navigateToDetailScreen()
        }
    }

    private fun navigateToDetailScreen() {
        recentNavigator.openMoreRecent(this)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                recentViewModel.limitedRecentSongs.collect { songs ->
                    adapter.updateSongs(songs)
                    if (songs.isNotEmpty()) {
                        binding.rvRecent.scrollToPosition(0)
                    }
                    binding.progressRecentHeard.visibility = View.GONE
                    if (songs.isEmpty()) {
                        binding.recentEmptyLayout.visibility = View.VISIBLE
                        binding.recentContentFragment.visibility = View.GONE
                    } else {
                        binding.recentContentFragment.visibility = View.VISIBLE
                        binding.recentEmptyLayout.visibility = View.GONE
                    }
                }
            }
        }

//        playbackSharedViewModel.selectedSongIndex.observe(viewLifecycleOwner) { index ->
//            // todo
//        }
//
//        playbackSharedViewModel.previousPlaylistModel.observe(viewLifecycleOwner) {
//        }
    }

    internal class MyLayoutManager(
        context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean
    ) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {
        override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
            val deltaX = (MusicAppUtils.DEFAULT_MARGIN_END * MusicAppUtils.DENSITY).toInt()
            lp.width = width - deltaX
            return true
        }
    }
}