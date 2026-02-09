package pro.branium.feature_playlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pro.branium.feature_playlist.ui.menu.OptionMenuUtils
import pro.branium.feature_playlist.ui.menu.PlaylistOptionMenuItem
import javax.inject.Inject

@HiltViewModel
class PlaylistOptionMenuViewModel @Inject constructor() : ViewModel() {
    private val _menuItems = MutableLiveData<List<PlaylistOptionMenuItem>>()

    val menuItems: MutableLiveData<List<PlaylistOptionMenuItem>> = _menuItems

    init {
        _menuItems.value = OptionMenuUtils.playlistOptionMenuItems
    }
}