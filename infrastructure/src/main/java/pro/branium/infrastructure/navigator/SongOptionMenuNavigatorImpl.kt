package pro.branium.infrastructure.navigator

import androidx.fragment.app.FragmentManager
import pro.branium.core_model.SongModel
import pro.branium.core_navigation.navigator.SongOptionMenuNavigator
import pro.branium.feature_song.ui.dialogs.SongOptionMenuDialogFragment
import javax.inject.Inject

class SongOptionMenuNavigatorImpl @Inject constructor() : SongOptionMenuNavigator {
    override fun openSongOptionMenu(fragmentManager: FragmentManager, song: SongModel) {
        val dialog = SongOptionMenuDialogFragment.newInstance(song.id)
        dialog.show(fragmentManager, SongOptionMenuDialogFragment.TAG)
    }
}