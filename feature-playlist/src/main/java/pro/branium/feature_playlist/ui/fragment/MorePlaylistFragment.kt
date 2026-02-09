package pro.branium.feature_playlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_navigation.library.PlaylistDetailNavigator
import pro.branium.feature_playlist.R
import pro.branium.feature_playlist.databinding.FragmentMorePlaylistBinding
import pro.branium.feature_playlist.ui.adapter.PlaylistAdapter
import pro.branium.feature_playlist.ui.base.PlaylistBaseFragment
import pro.branium.feature_playlist.ui.dialogs.PlaylistOptionMenuDialogFragment
import javax.inject.Inject

@AndroidEntryPoint
class MorePlaylistFragment : PlaylistBaseFragment() {
    @Inject
    lateinit var playlistDetailNavigator: PlaylistDetailNavigator
    private lateinit var binding: FragmentMorePlaylistBinding
    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMorePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.toolbarMorePlaylist.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        adapter = PlaylistAdapter(
            object : PlaylistAdapter.OnPlaylistClickListener {
                override fun onPlaylistClick(playlistId: Long) {
                    navigateToPlaylistDetail(playlistId)
                }

                override fun onPlaylistMenuOptionClick(playlistId: Long) {
                    val dialog = PlaylistOptionMenuDialogFragment(optionMenuListener)
                    dialog.show(
                        requireActivity().supportFragmentManager,
                        PlaylistOptionMenuDialogFragment.TAG
                    )
                }
            }
        )
        binding.rvMorePlaylist.adapter = adapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playlistViewModel.allPlaylist.collect { playlists ->
                    adapter.updatePlaylists(playlists)
                    if (playlists.isEmpty()) {
                        binding.playlistEmptyLayout.visibility = View.VISIBLE
                        binding.playlistContentLayout.visibility = View.GONE
                        binding.includeEmptyContent.tvEmptyMessage.text =
                            getString(R.string.message_no_playlist)
                    } else {
                        binding.playlistEmptyLayout.visibility = View.GONE
                        binding.playlistContentLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
        playlistViewModel.playlistWithSongs.observe(viewLifecycleOwner) { playlist ->
//            adapter.updatePlaylists(playlist)
        }
    }

    override fun navigateToPlaylistDetail(playlistId: Long) {
        playlistDetailNavigator.openPlaylistDetail(this, playlistId)
    }
}