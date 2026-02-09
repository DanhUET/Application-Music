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
import pro.branium.core_navigation.library.PlaylistNavigator
import pro.branium.feature_playlist.R
import pro.branium.feature_playlist.databinding.FragmentPlaylistBinding
import pro.branium.feature_playlist.ui.adapter.PlaylistAdapter
import pro.branium.feature_playlist.ui.base.PlaylistBaseFragment
import pro.branium.feature_playlist.ui.dialogs.PlaylistCreationDialogFragment
import pro.branium.feature_playlist.ui.dialogs.PlaylistOptionMenuDialogFragment
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : PlaylistBaseFragment() {
    @Inject
    lateinit var playlistNavigator: PlaylistNavigator

    @Inject
    lateinit var playlistDetailNavigator: PlaylistDetailNavigator
    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observePlaylist()
    }

    private fun setupView() {
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
        binding.rvPlaylist.adapter = adapter
        binding.includePlaylistCreation.layoutBtnCreatePlaylist.setOnClickListener {
            showDialogToCreatePlaylist()
        }
        binding.includePlaylistCreation.btnCreatePlaylist.setOnClickListener {
            showDialogToCreatePlaylist()
        }
        binding.layoutMorePlaylist.setOnClickListener {
            navigateToMorePlaylist()
        }
    }

    private fun showDialogToCreatePlaylist() {
        val listener = object : PlaylistCreationDialogFragment.OnClickListener {
            override fun onClick(playlistName: String) {
                playlistViewModel.createNewPlaylist(playlistName)
            }
        }
        val textChangeListener = object : PlaylistCreationDialogFragment.OnTextChangeListener {
            override fun onTextChange(playlistName: String) {
                playlistViewModel.findPlaylistByName(playlistName)
            }
        }
        val titleId = pro.branium.core_resources.R.string.title_create_playlist
        val positiveButtonTextId = pro.branium.core_resources.R.string.action_create
        val dialog = PlaylistCreationDialogFragment(
            titleId,
            positiveButtonTextId,
            listener,
            textChangeListener
        )
        val tag = PlaylistCreationDialogFragment.TAG
        dialog.show(requireActivity().supportFragmentManager, tag)
    }

    private fun navigateToMorePlaylist() {
        playlistNavigator.openMorePlaylist(this)
    }

    private fun observePlaylist() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playlistViewModel.limitedPlaylists.collect { playlists ->
                    adapter.updatePlaylists(playlists)
                    if (playlists.isEmpty()) {
                        binding.playlistEmptyLayout.visibility = View.VISIBLE
                        binding.playlistContentLayout.visibility = View.GONE
                        binding.includeEmptyContent.tvEmptyMessage.text =
                            getString(R.string.message_no_playlist)
                    } else {
                        binding.playlistContentLayout.visibility = View.VISIBLE
                        binding.playlistEmptyLayout.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun navigateToPlaylistDetail(playlistId: Long) {
        playlistDetailNavigator.openPlaylistDetail(this, playlistId)
    }
}