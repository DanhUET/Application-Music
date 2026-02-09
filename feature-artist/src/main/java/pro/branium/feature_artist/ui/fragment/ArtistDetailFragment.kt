package pro.branium.feature_artist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_ui.adapter.SongAdapter
import pro.branium.core_ui.mapper.toModel
import pro.branium.core_ui.model.DisplayArtistModel
import pro.branium.feature_artist.R
import pro.branium.feature_artist.databinding.FragmentArtistDetailBinding
import pro.branium.feature_artist.viewmodel.ArtistDetailViewModel
import pro.branium.presentation_common.base.BaseFragment
import kotlin.getValue

@AndroidEntryPoint
class ArtistDetailFragment : BaseFragment() {
    private lateinit var binding: FragmentArtistDetailBinding
    private lateinit var adapter: SongAdapter
    private val args: ArtistDetailFragmentArgs by navArgs()
    private val artistDetailViewModel: ArtistDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
        processData()
    }

    private fun setupView() {
        binding.toolbarArtistDetail.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                val playlist = artistDetailViewModel.playlist
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
        binding.includeDetailArtistSongList.rvSongList.adapter = adapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                artistDetailViewModel.songs.collect { songs ->
                    adapter.updateSongs(songs)
                    artistDetailViewModel.updatePlaylistSongs(songs)
                }
            }
        }

        artistDetailViewModel.artistModel.observe(viewLifecycleOwner) { artist ->
            if (artist != null) {
                binding.artistDetailLayout.visibility = View.VISIBLE
                binding.artistDetailEmptyLayout.visibility = View.GONE
                showArtistInfo(artist)
            } else {
                binding.artistDetailEmptyLayout.visibility = View.VISIBLE
                binding.artistDetailLayout.visibility = View.GONE
                val message = getString(R.string.message_no_artist_info)
                binding.includeEmptyLayout.tvEmptyMessage.text = message
            }
        }
    }

    private fun showArtistInfo(artistModel: DisplayArtistModel) {
        binding.artist = artistModel
    }

    private fun processData() {
        val artistId = args.artistId
        artistDetailViewModel.getArtist(artistId)
        artistDetailViewModel.getSongsByArtist(artistId)
    }
}