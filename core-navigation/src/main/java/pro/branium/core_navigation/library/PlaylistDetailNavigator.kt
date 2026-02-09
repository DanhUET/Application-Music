package pro.branium.core_navigation.library

import androidx.fragment.app.Fragment

interface PlaylistDetailNavigator {
    fun openPlaylistDetail(from: Fragment, playlistId: Long)
}