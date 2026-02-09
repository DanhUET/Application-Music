package pro.branium.feature_song.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_model.SongModel
import pro.branium.core_resources.R
import pro.branium.core_ui.extensions.showToast
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.feature_song.action.SongActionDispatcher
import pro.branium.feature_song.databinding.FragmentSongOptionMenuDialogBinding
import pro.branium.feature_song.ui.adapter.MenuItemAdapter
import pro.branium.feature_song.ui.menu.SongMenuAggregator
import pro.branium.feature_song.viewmodel.SongMenuViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SongOptionMenuDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSongOptionMenuDialogBinding
    private lateinit var adapter: MenuItemAdapter
    private val viewModel: SongMenuViewModel by activityViewModels()

    //    private val playlistViewModel: PlaylistViewModel by activityViewModels()
//    private val favoriteViewModel: FavoriteSongViewModel by activityViewModels()
    private var isClicked = false

    // inject aggregator và dispatcher
    @Inject
    lateinit var songMenuAggregator: SongMenuAggregator

    @Inject
    lateinit var songActionDispatcher: SongActionDispatcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongOptionMenuDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        processData()
        setupView()
        setupObserver()
    }

    private fun processData() {
        val songId = arguments?.getString(ARG_SONG_ID)
        songId?.let {
            viewModel.getSong(it)
        }
    }

    private fun setupView() {
        adapter = MenuItemAdapter(
            optionMenuClick = { item ->
                onMenuItemClick(item)
            }
        )
        binding.rvOptionMenu.adapter = adapter

        viewModel.songModel.value?.let { song ->
            val menuItems = songMenuAggregator.getMenuFor(song)
            adapter.updateMenuItems(menuItems)
        }
        binding.includeSongBottomSheet.btnOptionItemShare.setOnClickListener {
            requireContext().showToast(R.string.message_function_implementing)
            dismiss()
        }
    }

    private fun onMenuItemClick(item: SongMenuItem) {
        // forward action for dispatcher
        lifecycleScope.launch {
            songActionDispatcher.dispatch(item.action, requireView())
            dismiss()
        }
    }

    private fun setupObserver() {
        viewModel.songModel.observe(viewLifecycleOwner) { song ->
//            favoriteViewModel.setSongId(song.id)
            showSongInfo(song)
            // update song info with new favorite status change
            val menuItems = songMenuAggregator.getMenuFor(song)
            adapter.updateMenuItems(menuItems)
        }

//        favoriteViewModel.isFavorite.asLiveData().observe(viewLifecycleOwner) { status ->
//            viewModel.songModel.value?.let { song ->
//                val menuItems = songMenuAggregator.getMenuFor(song)
//                adapter.updateMenuItems(menuItems)
//            }
//        }
//
//        playlistViewModel.addResult.observe(viewLifecycleOwner) { addResult ->
//            if (isClicked) {
//                val messageId = if (addResult) {
//                    pro.branium.core.R.string.add_to_playlist_success
//                } else {
//                    pro.branium.core.R.string.add_to_playlist_failed
//                }
//                val playlistName = viewModel.playlistName.value ?: ""
//                val message = requireActivity().getString(messageId, playlistName)
//                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//                isClicked = false
//            }
//        }
    }

    private fun showSongInfo(songModel: SongModel) {
        binding.includeSongBottomSheet.textOptionItemSongTitle.text = songModel.title
        binding.includeSongBottomSheet.textOptionItemSongArtist.text = songModel.artist
        Glide.with(requireContext())
            .load(songModel.artworkUrl)
            .error(R.drawable.ic_album)
            .into(binding.includeSongBottomSheet.imageOptionSongArtwork)
    }

    companion object {
        private const val ARG_SONG_ID = "arg_song_id"
        const val TAG = "SongOptionMenuDialogFragment"

        fun newInstance(songId: String): SongOptionMenuDialogFragment {
            return SongOptionMenuDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SONG_ID, songId)
                }
            }
        }
    }
}