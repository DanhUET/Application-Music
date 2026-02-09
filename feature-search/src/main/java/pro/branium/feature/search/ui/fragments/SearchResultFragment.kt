package pro.branium.feature.search.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_ui.adapter.SongAdapter
import pro.branium.core_ui.mapper.toModel
import pro.branium.feature.search.viewmodel.SearchingViewModel
import pro.branium.presentation_common.base.BaseFragment
import pro.branium.search.databinding.FragmentSearchResultBinding


@AndroidEntryPoint
class SearchResultFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var adapter: SongAdapter
    private val viewModel: SearchingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeData()
    }

    private fun setupAdapter() {
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                viewModel.insertSearchedSongs(songModel)
                val playlist = viewModel.searchedPlaylist
                playbackController.prepareToPlay(
                    viewLifecycleOwner,
                    true,
                    {
                        showNetworkError(binding.root, binding.root)
                    },
                    songModel = songModel,
                    playlist,
                    index
                )
                viewModel.shouldSaveSearchedKey(true)
            },
            onSongOptionMenuClick = { songModel ->
                playbackController.showOptionMenu(
                    true,
                    {
                        showNetworkError(binding.root, binding.root)
                    },
                    parentFragmentManager,
                    songModel.toModel()
                )
            }
        )
        binding.rvSearchedSong.adapter = adapter
    }

    private fun observeData() {
        viewModel.songs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs)
            if (songs.isEmpty()) {
                binding.searchEmptyLayout.visibility = View.VISIBLE
            } else {
                binding.searchEmptyLayout.visibility = View.GONE
            }
        }
    }
}
