package pro.branium.feature_foryou.ui.fragment

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
import pro.branium.feature_foryou.databinding.FragmentMoreForYouBinding
import pro.branium.feature_foryou.viewmodel.ForYouViewModel
import pro.branium.presentation_common.base.BaseFragment

@AndroidEntryPoint
class MoreForYouFragment : BaseFragment() {
    private lateinit var binding: FragmentMoreForYouBinding
    private lateinit var adapter: SongAdapter

    private val forYouViewModel: ForYouViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreForYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.toolbarMoreForYou.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                val playlist = forYouViewModel.playlist
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
        binding.includeSongList.rvSongList.adapter = adapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forYouViewModel.top40ForYouSongs.collect { songs ->
                    adapter.updateSongs(songs)
                    if (songs.isEmpty()) {
                        binding.forYouEmptyLayout.visibility = View.VISIBLE
                        binding.forYouContentLayout.visibility = View.GONE
                    } else {
                        binding.forYouEmptyLayout.visibility = View.GONE
                        binding.forYouContentLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}