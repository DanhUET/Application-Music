package pro.branium.feature_mostheard.viewmodel

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
import pro.branium.core_playback.usecase.GetCurrentPlaylistUseCase
import pro.branium.core_playback.usecase.GetPlayingSongUseCase
import pro.branium.core_ui.mapper.toDisplayModels
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.core_utils.MusicAppUtils
import pro.branium.feature_mostheard.domain.usecase.GetMostHeardSongsUseCase
import javax.inject.Inject

@HiltViewModel
class MostHeardViewModel @Inject constructor(
    private val mostHeardSongsUseCase: GetMostHeardSongsUseCase,
    getPlayingSongUseCase: GetPlayingSongUseCase,
    getCurrentPlaylistUseCase: GetCurrentPlaylistUseCase
) : ViewModel() {
    var playlist = MusicAppUtils.defaultPlaylists[4]!!
    private val _top15MostHeardSongs =
        MutableStateFlow<List<DisplaySongModel>>(emptyList()).also { flow ->
            viewModelScope.launch(Dispatchers.IO) {
                mostHeardSongsUseCase(TOP_15_MOST_HEARD_LIMIT)
                    .collect { songs ->
                        flow.value = songs.toDisplayModels()
                        playlist = playlist.copy(songs = songs)
                    }
            }
        }
    private val _top40MostHeardSongs =
        MutableStateFlow<List<DisplaySongModel>>(emptyList()).also { flow ->
            viewModelScope.launch(Dispatchers.IO) {
                mostHeardSongsUseCase(TOP_40_MOST_HEARD_LIMIT)
                    .collect { songs ->
                        flow.value = songs.toDisplayModels()
                        playlist = playlist.copy(songs = songs)
                    }
            }
        }
    val top15MostHeardSongs: StateFlow<List<DisplaySongModel>> =
        _top15MostHeardSongs.combine(getPlayingSongUseCase()) { base, now ->
            val currentId = now?.id
            val playing = now?.isPlaying ?: return@combine emptyList()
            val currentPlaylist = getCurrentPlaylistUseCase().value
            base.map { m ->
                if (currentPlaylist?.playlistId == playlist.playlistId) {
                    val copyObj = m.copy(
                        isNowPlaying = (m.song.id == currentId),
                        isPlaying = playing && (m.song.id == currentId)
                    )
                    copyObj
                } else m
            }
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    val top40MostHeardSongs: StateFlow<List<DisplaySongModel>> =
        _top40MostHeardSongs.combine(getPlayingSongUseCase()) { base, now ->
            val currentId = now?.id
            val playing = now?.isPlaying ?: return@combine emptyList()
            val currentPlaylist = getCurrentPlaylistUseCase().value
            base.map { m ->
                if (currentPlaylist?.playlistId == playlist.playlistId) {
                    val copyObj = m.copy(
                        isNowPlaying = (m.song.id == currentId),
                        isPlaying = playing && (m.song.id == currentId)
                    )
                    copyObj
                } else m
            }
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    companion object {
        const val TOP_15_MOST_HEARD_LIMIT = 15
        const val TOP_40_MOST_HEARD_LIMIT = 40
    }
}