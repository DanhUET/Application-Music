package pro.branium.infrastructure.navigator

import androidx.fragment.app.FragmentManager
import pro.branium.core_navigation.navigator.SongInfoNavigator
import pro.branium.feature_song.ui.dialogs.DialogSongInfoFragment
import javax.inject.Inject

class SongInfoNavigatorImpl @Inject constructor() : SongInfoNavigator {
    override fun openSongInfo(fragmentManager: FragmentManager, songId: String) {
        val dialog = DialogSongInfoFragment.newInstance(songId)
        dialog.show(fragmentManager, DialogSongInfoFragment.TAG)
    }
}