package pro.branium.feature_playlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_model.SongModel
import pro.branium.core_ui.adapter.SongAdapter
import pro.branium.core_ui.mapper.toModel
import pro.branium.core_ui.mapper.toSongModels
import pro.branium.feature_playlist.R
import pro.branium.feature_playlist.databinding.FragmentPlaylistDetailBinding
import pro.branium.feature_playlist.viewmodel.PlaylistDetailViewModel
import pro.branium.presentation_common.base.BaseFragment

@AndroidEntryPoint
class PlaylistDetailFragment : BaseFragment() {
    private lateinit var binding: FragmentPlaylistDetailBinding
    private lateinit var adapter: SongAdapter
    private val args: PlaylistDetailFragmentArgs by navArgs()

    private val playlistDetailViewModel: PlaylistDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        processData()
        setupView()
        observePlaylistDetailViewModel()
    }

    private fun setupView() {
        binding.includePlaylistDetail
            .toolbarPlaylistDetail
            .setNavigationOnClickListener {
                requireActivity().supportFragmentManager
                    .popBackStack()
            }
        val title = getString(R.string.title_playlist_detail)
        binding
            .includePlaylistDetail
            .textPlaylistDetailToolbarTitle
            .text = title
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                val playlist = playlistDetailViewModel.selectedPlaylist.value
                playlist?.let {
                    playbackController.prepareToPlay(
                        viewLifecycleOwner,
                        true,
                        {
                            showNetworkError(binding.root, binding.root)
                        },
                        songModel,
                        it.toModel(),
                        index
                    )
                }
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
        binding.includePlaylistDetail
            .rvSongList
            .adapter = adapter
    }

    private fun processData() {
        playlistDetailViewModel.loadPlaylist(args.playlistId)
    }

    private fun observePlaylistDetailViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playlistDetailViewModel.selectedPlaylist.collect { playlist ->
                    playlist?.let {
                        val playlistName =
                            getString(R.string.text_playlist_name_detail, it.name)
                        binding.includePlaylistDetail
                            .textPlaylistDetailTitle.text = playlistName
                        adapter.updateSongs(it.songs)
                        showPlaylistInfo(it.songs.toSongModels())
                        if (it.songs.isEmpty()) {
                            binding.includePlaylistDetail.emptyLayout.visibility = View.VISIBLE
                            binding.includePlaylistDetail.contentLayout.visibility = View.GONE
                        } else {
                            binding.includePlaylistDetail.emptyLayout.visibility = View.GONE
                            binding.includePlaylistDetail.contentLayout.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun showPlaylistInfo(songModels: List<SongModel>) {
        val numberOfSong = getString(R.string.text_number_of_songs, songModels.size)
        binding.includePlaylistDetail
            .textPlaylistDetailNumOfSong.text = numberOfSong
        val artworkId = songModels.firstOrNull()?.artworkUrl
        Glide.with(this)
            .load(artworkId)
            .error(R.drawable.ic_album)
            .into(binding.includePlaylistDetail.imagePlaylistArtwork)
    }
}
