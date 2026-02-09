package pro.branium.feature_favorite.ui.fragment

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
import pro.branium.feature_favorite.databinding.FragmentMoreFavoriteBinding
import pro.branium.feature_favorite.viewmodel.FavoriteViewModel
import pro.branium.presentation_common.base.BaseFragment

@AndroidEntryPoint
class MoreFavoriteFragment : BaseFragment() {
    private lateinit var binding: FragmentMoreFavoriteBinding
    private lateinit var adapter: SongAdapter
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.toolbarMoreFavorite.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                playbackController.prepareToPlay(
                    viewLifecycleOwner,
                    true,
                    {
                        showNetworkError(binding.root, binding.root)
                    },
                    songModel,
                    favoriteViewModel.moreFavoriteSongsPlaylist,
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
                favoriteViewModel.moreFavoriteSongs.collect { songs ->
                    adapter.updateSongs(songs)
                    if (songs.isEmpty()) {
                        binding.favoriteEmptyLayout.visibility = View.VISIBLE
                        binding.favoriteContentLayout.visibility = View.GONE
                    } else {
                        binding.favoriteEmptyLayout.visibility = View.GONE
                        binding.favoriteContentLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}