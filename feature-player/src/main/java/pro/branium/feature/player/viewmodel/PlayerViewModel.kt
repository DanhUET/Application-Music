package pro.branium.feature.player.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pro.branium.core_ui.model.PlayerUiState
import javax.inject.Inject

class PlayerViewModel @Inject constructor() : ViewModel() {
    private val _playerUiState = MutableStateFlow(PlayerUiState.HIDDEN)
    val playerUiState: StateFlow<PlayerUiState> = _playerUiState

    fun onSongStarted() {
        _playerUiState.value = PlayerUiState.MINI_VISIBLE
    }

    fun onExpandPlayer() {
        _playerUiState.value = PlayerUiState.FULL_VISIBLE
    }

    fun onCollapsePlayer() {
        _playerUiState.value = PlayerUiState.MINI_VISIBLE
    }

    fun onNoSong() {
        _playerUiState.value = PlayerUiState.HIDDEN
    }
}