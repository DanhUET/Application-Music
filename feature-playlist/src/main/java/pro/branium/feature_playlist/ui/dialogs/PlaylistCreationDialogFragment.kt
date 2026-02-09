package pro.branium.feature_playlist.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.feature_playlist.R
import pro.branium.feature_playlist.viewmodel.PlaylistViewModel

@AndroidEntryPoint
class PlaylistCreationDialogFragment(
    private val titleId: Int,
    private val positiveButtonTextId: Int,
    private val listener: OnClickListener,
    private val textChangeListener: OnTextChangeListener,
    private val isEdit: Boolean = false
) : DialogFragment() {
    private val playlistViewModel: PlaylistViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val layoutInflater = requireActivity().layoutInflater
        val rootView = layoutInflater.inflate(R.layout.fragment_dialog_playlist_creation, null)
        val editPlaylistName = rootView.findViewById<TextInputEditText>(R.id.edit_playlist_name)
        val resId = pro.branium.core_resources.R.string.action_cancel
        builder.setView(rootView)
            .setTitle(getString(titleId))
            .setPositiveButton(getString(positiveButtonTextId)) { _, _ ->
                setupPositiveButton(editPlaylistName)
            }
            .setNegativeButton(getString(resId)) { _, _ ->
                dismiss()
            }
        val text = if (isEdit) playlistViewModel.selectedPlaylistModel.name else null
        editPlaylistName.setText(text)
        setupEditTextListener(editPlaylistName)
        playlistViewModel.findPlaylistByName("")
        observeData(editPlaylistName)
        return builder.create()
    }

    private fun setupPositiveButton(editPlaylistName: TextInputEditText) {
        if (editPlaylistName.text != null) {
            val playlistName = editPlaylistName.text.toString().trim()
            if (playlistName.isNotEmpty()) {
                if (editPlaylistName.error == null) {
                    listener.onClick(playlistName)
                }
            } else {
                val resId = pro.branium.core_resources.R.string.error_empty_playlist_name
                val message = getString(resId)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupEditTextListener(editPlaylistName: TextInputEditText) {
        editPlaylistName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                requireActivity().window
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            }
        }
        editPlaylistName.doOnTextChanged { text, _, _, _ ->
            textChangeListener.onTextChange(text.toString())
        }
    }

    private fun observeData(editPlaylistName: TextInputEditText) {
        playlistViewModel.searchedPlaylistModel.observe(requireActivity()) { playlist ->
            if (playlist != null) {
                val message = editPlaylistName.context.getString(R.string.error_playlist_exists)
                editPlaylistName.error = message
            } else {
                editPlaylistName.error = null
            }
        }
    }

    interface OnClickListener {
        fun onClick(playlistName: String)
    }

    interface OnTextChangeListener {
        fun onTextChange(playlistName: String)
    }

    companion object {
        const val TAG = "DialogPlaylistCreationFragment"
    }
}