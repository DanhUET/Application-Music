package pro.branium.feature_album.ui.fragment

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
import pro.branium.feature_album.databinding.FragmentAlbumDetailBinding
import pro.branium.feature_album.viewmodel.AlbumDetailsViewModel
import pro.branium.presentation_common.base.BaseFragment

@AndroidEntryPoint
class AlbumDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentAlbumDetailBinding
    private lateinit var adapter: SongAdapter
    private val args: AlbumDetailsFragmentArgs by navArgs()

    private val viewModel: AlbumDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        processData()
        observeViewModel()
    }

    private fun setupView() {
        binding.includeAlbumDetail.pbAlbumSongs.visibility = View.VISIBLE
        binding.includeAlbumDetail.toolbarPlaylistDetail.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                val playlist = viewModel.playlistModel
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
            onSongOptionMenuClick = { displaySong ->
                playbackController.showOptionMenu(
                    true,
                    {
                        showNetworkError(binding.root, binding.root)
                    },
                    childFragmentManager,
                    displaySong.toModel()
                )
            }
        )
        binding.includeAlbumDetail.rvSongList.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.albumModel.observe(viewLifecycleOwner) { album ->
            if (album != null) {
                binding.album = album
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.songs.collect { songs ->
                    binding.includeAlbumDetail.pbAlbumSongs.visibility = View.GONE
                    adapter.updateSongs(songs)
                    if (songs.isEmpty()) {
                        binding.includeAlbumDetail.contentLayout.visibility = View.GONE
                        binding.includeAlbumDetail.emptyLayout.visibility = View.VISIBLE
                    } else {
                        binding.includeAlbumDetail.contentLayout.visibility = View.VISIBLE
                        binding.includeAlbumDetail.emptyLayout.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun processData() {
        val isNetworkAvailable = networkViewModel.isNetworkAvailable.value
        if (isNetworkAvailable == true) {
            val albumId = args.albumId
            viewModel.loadAlbumDetails(albumId)
            viewModel.loadSongsForAlbum(albumId)
        } else {
            showNetworkError(binding.root, binding.root)
        }
    }
}