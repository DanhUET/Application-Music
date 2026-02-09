package pro.branium.feature_mostheard.ui.fragment

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
import pro.branium.feature_mostheard.databinding.FragmentMoreMostHeardBinding
import pro.branium.feature_mostheard.viewmodel.MostHeardViewModel
import pro.branium.presentation_common.base.BaseFragment

@AndroidEntryPoint
class MoreMostHeardFragment : BaseFragment() {
    private lateinit var binding: FragmentMoreMostHeardBinding
    private lateinit var adapter: SongAdapter

    private val mostHeardViewModel: MostHeardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreMostHeardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.toolbarMoreMostHeard.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                val playlist = mostHeardViewModel.playlist
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
                mostHeardViewModel.top40MostHeardSongs.collect { songs ->
                    adapter.updateSongs(songs)
                    if (songs.isEmpty()) {
                        binding.mostHeardEmptyLayout.visibility = View.VISIBLE
                        binding.mostHeardContentLayout.visibility = View.GONE
                    } else {
                        binding.mostHeardEmptyLayout.visibility = View.GONE
                        binding.mostHeardContentLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}