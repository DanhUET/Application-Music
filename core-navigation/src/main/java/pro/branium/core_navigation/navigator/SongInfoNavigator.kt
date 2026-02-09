package pro.branium.core_navigation.navigator

import androidx.fragment.app.FragmentManager

interface SongInfoNavigator {
    fun openSongInfo(fragmentManager: FragmentManager, songId: String)
}