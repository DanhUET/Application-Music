package pro.branium.feature_favorite.viewmodel

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
import pro.branium.core_utils.MusicAppUtils.defaultPlaylists
import pro.branium.feature_favorite.domain.usecase.GetLimitedFavoriteSongsUseCase
import pro.branium.feature_favorite.domain.usecase.GetMoreFavoriteSongsUseCase
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getLimitedFavoriteSongUseCase: GetLimitedFavoriteSongsUseCase,
    private val getMoreFavoriteSongUseCase: GetMoreFavoriteSongsUseCase,
    getPlayingSongUseCase: GetPlayingSongUseCase,
    getCurrentPlaylistUseCase: GetCurrentPlaylistUseCase,
) : ViewModel() {
    var topFavoriteSongsPlaylist = defaultPlaylists[2]!!
    var moreFavoriteSongsPlaylist = defaultPlaylists[10]!!
    private val _limitedFavoriteSongs =
        MutableStateFlow<List<DisplaySongModel>>(emptyList()).also { flow ->
            viewModelScope.launch(Dispatchers.IO) {
                getLimitedFavoriteSongUseCase(LIMIT)
                    .collect { songs ->
                        flow.value = songs.toDisplayModels()
                        topFavoriteSongsPlaylist = topFavoriteSongsPlaylist.copy(songs = songs)
                    }
            }
        }
    private val _moreFavoriteSongs =
        MutableStateFlow<List<DisplaySongModel>>(emptyList()).also { flow ->
            viewModelScope.launch(Dispatchers.IO) {
                getMoreFavoriteSongUseCase()
                    .collect { songs ->
                        flow.value = songs.toDisplayModels()
                        moreFavoriteSongsPlaylist = moreFavoriteSongsPlaylist.copy(songs = songs)
                    }
            }
        }
    val limitedFavoriteSongs: StateFlow<List<DisplaySongModel>> =
        _limitedFavoriteSongs.combine(getPlayingSongUseCase()) { base, now ->
            val currentId = now?.id
            val playing = now?.isPlaying ?: return@combine emptyList()
            val currentPlaylist = getCurrentPlaylistUseCase().value
            base.map { m ->
                if (currentPlaylist?.playlistId == topFavoriteSongsPlaylist.playlistId ||
                    currentPlaylist?.playlistId == moreFavoriteSongsPlaylist.playlistId
                ) {
                    val copyObj = m.copy(
                        isNowPlaying = (m.song.id == currentId),
                        isPlaying = playing && (m.song.id == currentId)
                    )
                    copyObj
                } else {
                    m
                }
            }
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    val moreFavoriteSongs: StateFlow<List<DisplaySongModel>> =
        _moreFavoriteSongs.combine(getPlayingSongUseCase()) { base, now ->
            val currentId = now?.id
            val playing = now?.isPlaying ?: return@combine emptyList()
            val currentPlaylist = getCurrentPlaylistUseCase().value
            base.map { m ->
                if (currentPlaylist?.playlistId == topFavoriteSongsPlaylist.playlistId ||
                    currentPlaylist?.playlistId == moreFavoriteSongsPlaylist.playlistId
                ) {
                    val copyObj = m.copy(
                        isNowPlaying = (m.song.id == currentId),
                        isPlaying = playing && (m.song.id == currentId)
                    )
                    copyObj
                } else {
                    m
                }
            }
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    companion object {
        const val LIMIT = 15
    }
}