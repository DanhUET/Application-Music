package pro.branium.feature_recent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_ui.adapter.SongAdapter
import pro.branium.core_ui.mapper.toModel
import pro.branium.feature_recent.databinding.FragmentMoreRecentBinding
import pro.branium.feature_recent.viewmodel.RecentViewModel
import pro.branium.presentation_common.base.BaseFragment

@AndroidEntryPoint
class MoreRecentFragment : BaseFragment() {
    private lateinit var binding: FragmentMoreRecentBinding
    private lateinit var adapter: SongAdapter

    private val recentViewModel: RecentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreRecentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.toolbarMoreRecentSong.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                playbackController.prepareToPlay(
                    viewLifecycleOwner,
                    true, {
                        showNetworkError(binding.root, binding.root)
                    },
                    songModel,
                    recentViewModel.playlist,
                    index
                )
            },
            onSongOptionMenuClick = { songModel ->
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
        )
        binding.includeSongList.rvSongList.adapter = adapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                recentViewModel.moreRecentSongs.collect { songs ->
                    adapter.updateSongs(songs)
                    if (songs.isEmpty()) {
                        binding.recentEmptyLayout.visibility = View.VISIBLE
                        binding.recentContentLayout.visibility = View.GONE
                    } else {
                        binding.recentEmptyLayout.visibility = View.GONE
                        binding.recentContentLayout.visibility = View.VISIBLE
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
}