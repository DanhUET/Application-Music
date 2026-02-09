package pro.branium.feature_recommended.ui.fragment

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
import pro.branium.core_navigation.home.RecommendedNavigator
import pro.branium.core_ui.adapter.SongAdapter
import pro.branium.core_ui.mapper.toModel
import pro.branium.feature_recommended.databinding.FragmentRecommendedBinding
import pro.branium.feature_recommended.viewmodel.RecommendedViewModel
import pro.branium.presentation_common.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class RecommendedFragment : BaseFragment() {
    @Inject
    lateinit var navigator: RecommendedNavigator
    private lateinit var binding: FragmentRecommendedBinding
    private var isDataLoaded = false

    private val recommendedViewModel: RecommendedViewModel by activityViewModels()

    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
        observeData()
    }

    private fun setupView() {
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                val networkState = networkViewModel.isNetworkAvailable.value
                if (networkState == true) {
                    val playlist = recommendedViewModel.topRecommendedPlaylist
                    playbackController.prepareToPlay(
                        viewLifecycleOwner,
                        true,
                        {
                            showNetworkError(binding.root, binding.root)
                        },
                        songModel,
                        playlist,
                        index
                    )
                } else {
                    showNetworkError(binding.root, binding.root)
                }
            },
            onSongOptionMenuClick = { songModel ->
                playbackController.showOptionMenu(
                    true,
                    { showNetworkError(binding.root, binding.root) },
                    parentFragmentManager,
                    songModel.toModel()
                )
            }
        )
        binding.includeSongList.rvSongList.setItemViewCacheSize(20)
        binding.includeSongList.rvSongList.adapter = adapter
        binding.layoutMoreRecommended.setOnClickListener {
            navigateToMoreRecommended()
        }
    }

    private fun setupViewModel() {
        networkViewModel.isNetworkAvailable.observe(viewLifecycleOwner) {
            if (it && !isDataLoaded) {
                recommendedViewModel.loadLimitedRecommendedSongs()
            } else if (!it) {
                showNetworkError(binding.root, binding.root)
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                recommendedViewModel.displayLimitedSongs.collect { songs ->
                    adapter.updateSongs(songs)
                }
            }
        }
    }

    private fun navigateToMoreRecommended() {
        navigator.openMoreRecommended(this)
    }
}