package pro.branium.feature.search.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_model.SongModel
import pro.branium.core_ui.adapter.SongAdapter
import pro.branium.core_ui.base.SnackBarAnchorProvider
import pro.branium.core_ui.callback.OnItemDeleteListener
import pro.branium.core_ui.callback.OnUndoListener
import pro.branium.core_ui.callback.SwipeToDeleteCallback
import pro.branium.core_ui.dialog.ConfirmationDialogFragment
import pro.branium.core_ui.mapper.toModel
import pro.branium.feature.search.viewmodel.SearchingViewModel
import pro.branium.presentation_common.base.BaseFragment
import pro.branium.search.R
import pro.branium.search.databinding.FragmentHistorySearchedSongBinding

@AndroidEntryPoint
class HistorySearchedSongFragment : BaseFragment() {
    private lateinit var binding: FragmentHistorySearchedSongBinding
    private lateinit var adapter: SongAdapter

    private val viewModel: SearchingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistorySearchedSongBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
        setupObserver()
    }

    private fun setupView() {
        adapter = SongAdapter(
            onSongClick = { songModel, index ->
                clearSearchViewFocus()
                playbackController.prepareToPlay(
                    viewLifecycleOwner,
                    true,
                    { showNetworkError(binding.root, binding.root) },
                    songModel
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
            },
            object : OnItemDeleteListener {
                override fun onItemDelete(
                    position: Int,
                    songModel: SongModel,
                    callback: OnUndoListener
                ) {
                    onDelete(songModel, callback)
                }
            }
        )
        binding.rvHistorySearchedSong.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvHistorySearchedSong)
        binding.titleRecentSearchedSong.setOnClickListener { clearSearchViewFocus() }
        binding.root.setOnClickListener { clearSearchViewFocus() }
    }

    private fun clearSearchViewFocus() {
        val searchView = requireActivity().findViewById<SearchView>(R.id.search_view_home)
        searchView.clearFocus()
    }

    private fun onDelete(songModel: SongModel, callback: OnUndoListener) {
        var isDeleted = true
        val message = getString(R.string.item_song_removed, songModel.title)
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.setAction(R.string.action_undo) {
            isDeleted = false
            callback.onUndo()
        }
        snackBar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (isDeleted) {
                    viewModel.deleteSearchedSong(songModel)
                }
            }
        })
        val anchorView1 = binding.root
//            requireActivity().findViewById<View>(pro.branium.presentation.R.id.mini_player)
        val anchorView2 = (activity as? SnackBarAnchorProvider)?.getSnackBarAnchorView()
        val selectedAnchorView =
            if (anchorView1.isGone) anchorView2 else anchorView1
        snackBar.anchorView = selectedAnchorView
        snackBar.show()
    }

    private fun setupListener() {
        binding.tvClearHistorySearchedSong.setOnClickListener {
            clearSearchViewFocus()
            if (viewModel.historySearchedSongs.value?.isNotEmpty() == true) {
                val messageId = R.string.message_confirm_clear_song_history
                val dialog = ConfirmationDialogFragment(
                    messageId = messageId,
                    listener = object : ConfirmationDialogFragment.OnDeleteConfirmListener {
                        override fun onConfirm(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                viewModel.clearAllSongs()
                            }
                        }
                    })
                dialog.show(
                    requireActivity().supportFragmentManager,
                    ConfirmationDialogFragment.TAG
                )
            } else {
                val snackBar = Snackbar.make(
                    requireView(),
                    R.string.message_no_recent_searched_song,
                    Snackbar.LENGTH_SHORT
                )
                snackBar.anchorView = binding.root
//                    requireActivity().findViewById(pro.branium.feature.song.R.id.mini_player)
                snackBar.show()
            }
        }
    }

    private fun setupObserver() {
        viewModel.historySearchedSongs.observe(viewLifecycleOwner) { songs ->
            adapter.updateSongs(songs)
            if (songs.isEmpty()) {
                binding.historySongEmptyLayout.visibility = View.VISIBLE
                binding.historySongContentLayout.visibility = View.GONE
            } else {
                binding.historySongEmptyLayout.visibility = View.GONE
                binding.historySongContentLayout.visibility = View.VISIBLE
            }
        }
    }
}