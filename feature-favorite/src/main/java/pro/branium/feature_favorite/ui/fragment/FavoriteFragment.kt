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
import pro.branium.core_navigation.library.FavoriteSongNavigator
import pro.branium.core_ui.adapter.SongAdapter
import pro.branium.core_ui.mapper.toModel
import pro.branium.feature_favorite.databinding.FragmentFavoriteBinding
import pro.branium.feature_favorite.viewmodel.FavoriteViewModel
import pro.branium.presentation_common.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseFragment() {
    @Inject
    lateinit var favoriteNavigator: FavoriteSongNavigator
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: SongAdapter

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                playbackController.prepareToPlay(
                    viewLifecycleOwner,
                    true,
                    {
                        showNetworkError(binding.root, binding.root)
                    },
                    songModel,
                    favoriteViewModel.topFavoriteSongsPlaylist,
                    index
                )
            },
            onSongOptionMenuClick = { song ->
                val networkState = networkViewModel.isNetworkAvailable.value
                if (networkState == true) {
                    playbackController.showOptionMenu(
                        true,
                        {
                            showNetworkError(binding.root, binding.root)
                        },
                        parentFragmentManager,
                        song.toModel()
                    )
                } else {
                    showNetworkError(binding.root, binding.root)
                }
            }
        )
        binding.includeFavorite.rvSongList.adapter = adapter
        binding.layoutMoreFavorite.setOnClickListener {
            navigateToMoreFavoriteScreen()
        }
    }

    private fun navigateToMoreFavoriteScreen() {
        favoriteNavigator.openMoreFavoriteSong(this)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.limitedFavoriteSongs.collect { songs ->
                    adapter.updateSongs(songs)
                    if (songs.isEmpty()) {
                        binding.favoriteEmptyLayout.visibility = View.VISIBLE
                        binding.favoriteContentLayout.visibility = View.GONE
                    } else {
                        binding.favoriteContentLayout.visibility = View.VISIBLE
                        binding.favoriteEmptyLayout.visibility = View.GONE
                    }
                }
            }
        }
    }
}