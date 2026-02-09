package pro.branium.feature_playlist.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_ui.base.PlaylistOptionMenuActionListener
import pro.branium.core_ui.dialog.ConfirmationDialogFragment
import pro.branium.core_ui.extensions.showToast
import pro.branium.core_ui.mapper.toModel
import pro.branium.feature_playlist.R
import pro.branium.feature_playlist.ui.dialogs.PlaylistCreationDialogFragment
import pro.branium.feature_playlist.viewmodel.PlaylistViewModel

@AndroidEntryPoint
abstract class PlaylistBaseFragment : Fragment() {
    protected val playlistViewModel: PlaylistViewModel by activityViewModels()
    protected val optionMenuListener = object : PlaylistOptionMenuActionListener {
        override fun onSynchronize() {
            showToast()
        }

        override fun onDelete() {
            showDeleteConfirmationDialog()
        }

        override fun onUpdate() {
            showEditPlaylistDialog()
        }

        override fun onViewDetail() {
            val playlist = playlistViewModel.selectedPlaylistModel
            navigateToPlaylistDetail(playlist.playlistId)
        }
    }

    protected abstract fun navigateToPlaylistDetail(playlistId: Long)

    protected fun showDeleteConfirmationDialog() {
        val message = getString(
            R.string.message_confirm_delete_playlist,
            playlistViewModel.selectedPlaylistModel.name
        )
        val listener = object : ConfirmationDialogFragment.OnDeleteConfirmListener {
            override fun onConfirm(isConfirmed: Boolean) {
                val playlist = playlistViewModel.selectedPlaylistModel
                if (isConfirmed) {
                    playlistViewModel.deletePlaylist(playlist.toModel())
                }
            }
        }
        showConfirmationDialog(message, listener)
    }

    private fun showConfirmationDialog(
        message: String,
        listener: ConfirmationDialogFragment.OnDeleteConfirmListener
    ) {
        val dialog = ConfirmationDialogFragment(message = message, listener = listener)
        dialog.show(
            requireActivity().supportFragmentManager,
            ConfirmationDialogFragment.TAG
        )
    }

    protected fun showEditPlaylistDialog() {
        val listener = object : PlaylistCreationDialogFragment.OnClickListener {
            override fun onClick(playlistName: String) {
                showUpdatePlaylistConfirmationDialog(playlistName)
            }
        }
        val textChangeListener = object : PlaylistCreationDialogFragment.OnTextChangeListener {
            override fun onTextChange(playlistName: String) {
                playlistViewModel.findPlaylistByName(playlistName)
            }
        }
        val titleId = R.string.title_edit_playlist
        val positiveButtonTextId = R.string.action_save
        val dialog = PlaylistCreationDialogFragment(
            titleId,
            positiveButtonTextId,
            listener,
            textChangeListener,
            true
        )
        val tag = PlaylistCreationDialogFragment.TAG
        dialog.show(requireActivity().supportFragmentManager, tag)
    }

    private fun showUpdatePlaylistConfirmationDialog(newName: String) {
        val message = getString(R.string.message_confirm_update_playlist)
        val listener = object : ConfirmationDialogFragment.OnDeleteConfirmListener {
            override fun onConfirm(isConfirmed: Boolean) {
                val playlist = playlistViewModel.selectedPlaylistModel.toModel()
                if (isConfirmed) {
                    playlistViewModel.updatePlaylist(playlist)
                }
            }
        }
        showConfirmationDialog(message, listener)
    }

    protected fun showToast() {
        requireContext().showToast(R.string.message_function_implementing)
    }
}