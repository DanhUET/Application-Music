package pro.branium.feature_playlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pro.branium.core_model.PlaylistModel
import pro.branium.core_playback.usecase.GetCurrentPlaylistUseCase
import pro.branium.core_playback.usecase.GetPlayingSongUseCase
import pro.branium.core_ui.mapper.markNowPlaying
import pro.branium.core_ui.mapper.toDisplayModel
import pro.branium.core_ui.model.DisplayPlaylist
import pro.branium.feature_playlist.domain.usecase.GetPlaylistWithSongsUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val getPlaylistWithSongsUseCase: GetPlaylistWithSongsUseCase,
    getPlayingSongUseCase: GetPlayingSongUseCase,
    getCurrentPlaylistUseCase: GetCurrentPlaylistUseCase
) : ViewModel() {
    private val _playlistModel = MutableStateFlow<PlaylistModel?>(null)

    val selectedPlaylist: StateFlow<DisplayPlaylist?> =
        combine(
            _playlistModel,
            getPlayingSongUseCase(),
            getCurrentPlaylistUseCase()
        ) { detail, now, currentPlaylist ->
            if (detail == null) return@combine null

            val currentId = now?.id
            val playing = now?.isPlaying ?: false

            DisplayPlaylist(
                playlistId = detail.playlistId,
                name = detail.name,
                artwork = detail.artwork,
                createdAt = detail.createdAt,
                songs = detail.songs
                    .map { it.toDisplayModel() }
                    .markNowPlaying(
                        currentPlaylist = currentPlaylist,
                        currentId = currentId,
                        isPlaying = playing,
                        detail.playlistId
                    )
            )
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )


    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = getPlaylistWithSongsUseCase(playlistId)
            playlist.collect {
                _playlistModel.value = it
            }
        }
    }
}