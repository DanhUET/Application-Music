package pro.branium.feature_playlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.branium.core_playback.usecase.GetCurrentPlaylistUseCase
import pro.branium.core_playback.usecase.SetCurrentPlaylistUseCase
import pro.branium.core_model.PlaylistModel
import pro.branium.core_utils.MusicAppUtils
import javax.inject.Inject

@HiltViewModel
class PlaylistContextViewModel @Inject constructor(
    getCurrentPlaylistUseCase: GetCurrentPlaylistUseCase,
    private val setCurrentPlaylistUseCase: SetCurrentPlaylistUseCase
) : ViewModel() {
    val currentPlaylist: StateFlow<PlaylistModel?> = getCurrentPlaylistUseCase()

    private val _previousPlaylist = MutableLiveData<PlaylistModel>()
    val previousPlaylist: LiveData<PlaylistModel> = _previousPlaylist

    fun setCurrentPlaylist(
        newPlaylist: PlaylistModel = PlaylistModel(
            1,
            MusicAppUtils.DefaultPlaylistName.RECOMMENDED.value
        )
    ) {
        if (currentPlaylist.value == newPlaylist) return
        _previousPlaylist.value = currentPlaylist.value
        viewModelScope.launch {
            setCurrentPlaylistUseCase(newPlaylist)
        }
    }
}