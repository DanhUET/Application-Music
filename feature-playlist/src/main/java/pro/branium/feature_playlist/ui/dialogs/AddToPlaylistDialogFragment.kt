package pro.branium.feature_playlist.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_model.PlaylistModel
import pro.branium.core_ui.model.DisplayPlaylist
import pro.branium.feature_playlist.R
import pro.branium.feature_playlist.ui.adapter.PlaylistAdapter
import pro.branium.feature_playlist.viewmodel.PlaylistViewModel

@AndroidEntryPoint
class AddToPlaylistDialogFragment(
    private val showOptionMenu: Boolean,
    private val onPlaylistSelected: (playlistId: Long) -> Unit
) : DialogFragment() {
    private lateinit var adapter: PlaylistAdapter
    private val viewModel: PlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupComponents()
        return inflater.inflate(R.layout.fragment_add_to_playlist_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_dialog_playlist)
        recyclerView.adapter = adapter

        val btnCancel = view.findViewById<MaterialButton>(R.id.btn_cancel)
        val btnCreate = view.findViewById<MaterialButton>(R.id.btn_create)
        val editPlaylistName = view.findViewById<TextInputEditText>(R.id.edit_playlist_name)

        btnCancel.setOnClickListener { dismiss() }
        btnCreate.setOnClickListener { createPlaylist(editPlaylistName) }

        observePlaylistList()
        observePlaylistName(editPlaylistName)
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        setupComponents()
//        val builder = AlertDialog.Builder(requireActivity())
//        val layoutInflater = requireActivity().layoutInflater
//        val rootView = layoutInflater.inflate(R.layout.fragment_add_to_playlist_dialog, null)
//        val recycleViewPlaylist = rootView.findViewById<RecyclerView>(R.id.rv_dialog_playlist)
//        recycleViewPlaylist.adapter = adapter
//        val btnCancel = rootView.findViewById<MaterialButton>(R.id.btn_cancel)
//        val btnCreate = rootView.findViewById<MaterialButton>(R.id.btn_create)
//        val editPlaylistName = rootView.findViewById<TextInputEditText>(R.id.edit_playlist_name)
//        btnCreate.setOnClickListener {
//            createPlaylist(editPlaylistName)
//        }
//        btnCancel.setOnClickListener {
//            dismiss()
//        }
//        builder.setView(rootView)
//        observePlaylistList()
//        observePlaylistName(editPlaylistName)
//        viewModel.findPlaylistByName("")
//        return builder.create()
//    }

    private fun setupComponents() {
        adapter = PlaylistAdapter(
            object : PlaylistAdapter.OnPlaylistClickListener {
                override fun onPlaylistClick(playlistId: Long) {
                    onPlaylistSelected(playlistId)
                    dismiss()
                }

                override fun onPlaylistMenuOptionClick(playlistId: Long) {
                    // todo
                }
            },
            showOptionMenu
        )
    }

    private fun createPlaylist(editPlaylistName: TextInputEditText) {
        if (editPlaylistName.text != null) {
            val newPlaylistName = editPlaylistName.text.toString().trim()
            if (newPlaylistName.isEmpty()) {
                editPlaylistName.error =
                    getString(pro.branium.core_resources.R.string.error_empty_playlist_name)
            }
            if (editPlaylistName.error == null) {
                viewModel.createNewPlaylist(newPlaylistName)
                editPlaylistName.text!!.clear()
                closeKeyboard(editPlaylistName)
            }
        }
    }

    private fun closeKeyboard(editPlaylistName: TextInputEditText) {
        val inputMethodManager = requireActivity()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editPlaylistName.windowToken, 0)
    }

    private fun observePlaylistList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allPlaylist.collect { playlists ->
                    adapter.updatePlaylists(playlists)
                }
            }
        }
    }

    private fun observePlaylistName(editPlaylistName: TextInputEditText) {
        editPlaylistName.doOnTextChanged { text, _, _, _ ->
            text?.let {
                viewModel.findPlaylistByName(it.toString())
            }
        }
        viewModel.searchedPlaylistModel.observe(requireActivity()) { playlists ->
            if (playlists == null) {
                editPlaylistName.error = null
            } else {
                editPlaylistName.error =
                    requireActivity().getString(R.string.error_playlist_exists)
            }
        }
    }

    companion object {
        const val TAG = "DialogAddSongToPlaylistFragment"
    }
}