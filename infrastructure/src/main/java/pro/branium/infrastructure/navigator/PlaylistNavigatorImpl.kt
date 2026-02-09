package pro.branium.infrastructure.navigator

import androidx.fragment.app.FragmentManager
import pro.branium.core_model.PlaylistModel
import pro.branium.core_navigation.navigator.PlaylistNavigator
import pro.branium.feature_playlist.ui.dialogs.AddToPlaylistDialogFragment
import javax.inject.Inject

class PlaylistNavigatorImpl @Inject constructor(
    // PlaylistManager/Usecase injection ?
) : PlaylistNavigator {
    override fun openAddToPlaylistDialog(
        fragmentManager: FragmentManager,
        onPlaylistSelected: (playlistId: Long) -> Unit
    ) {
        val dialog = AddToPlaylistDialogFragment(
            true,
            onPlaylistSelected = { playlist ->
                onPlaylistSelected(playlist)
            }
        )
        dialog.show(fragmentManager, AddToPlaylistDialogFragment.TAG)
    }
}