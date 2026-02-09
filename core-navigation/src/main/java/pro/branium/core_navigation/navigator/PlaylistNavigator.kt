package pro.branium.core_navigation.navigator

import androidx.fragment.app.FragmentManager

interface PlaylistNavigator {
    fun openAddToPlaylistDialog(
        fragmentManager: FragmentManager,
        onPlaylistSelected: (playlist: Long) -> Unit
    )
}