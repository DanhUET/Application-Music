package pro.branium.core_navigation.navigator

import androidx.fragment.app.FragmentManager
import pro.branium.core_model.SongModel

interface SongOptionMenuNavigator {
    fun openSongOptionMenu(fragmentManager: FragmentManager, song: SongModel)
}