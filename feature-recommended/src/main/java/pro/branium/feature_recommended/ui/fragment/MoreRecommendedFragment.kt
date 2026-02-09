package pro.branium.feature_recommended.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_ui.mapper.toModel
import pro.branium.feature_recommended.databinding.FragmentMoreRecommendedBinding
import pro.branium.feature_recommended.ui.adapter.SongPagingAdapter
import pro.branium.feature_recommended.viewmodel.RecommendedViewModel
import pro.branium.presentation_common.base.BaseFragment

@AndroidEntryPoint
class MoreRecommendedFragment : BaseFragment() {
    private lateinit var binding: FragmentMoreRecommendedBinding
    private lateinit var adapter: SongPagingAdapter

    private val recommendedViewModel: RecommendedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreRecommendedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    private fun setupView() {
        adapter = SongPagingAdapter(
            onSongClickListener = { songModel, index ->
                val networkState = networkViewModel.isNetworkAvailable.value
                if (networkState == true) {
                    val playlist = recommendedViewModel.recommendedPlaylist
                    playbackController.prepareToPlay(
                        viewLifecycleOwner,
                        true,
                        {
                            showNetworkError(binding.root, binding.root)
                        },
                        songModel.song,
                        playlist,
                        index
                    )
                } else {
                    showNetworkError(binding.root, binding.root)
                }
            },
            onSongOptionMenuClickListener = { songModel ->
                val networkState = networkViewModel.isNetworkAvailable.value
                if (networkState == true) {
                    playbackController.showOptionMenu(
                        true,
                        { showNetworkError(binding.root, binding.root) },
                        parentFragmentManager,
                        songModel.toModel()
                    )
                } else {
                    showNetworkError(binding.root, binding.root)
                }
            }
        )
        binding.includeMoreRecommended.rvSongList.adapter = adapter
        binding.toolbarMoreRecommended.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            recommendedViewModel.moreDisplayLimitedSongs.collect { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadStates ->
                val isNotLoading =
                    loadStates.source.refresh is androidx.paging.LoadState.NotLoading
                val hasData = adapter.itemCount > 0
                if (isNotLoading) {
                    if (hasData) {
                        binding.recommendedContentLayout.visibility = View.VISIBLE
                        binding.recommendedEmptyLayout.visibility = View.GONE
                    } else {
                        binding.recommendedEmptyLayout.visibility = View.VISIBLE
                        binding.recommendedContentLayout.visibility = View.GONE
                    }
                }
            }
        }
    }
}