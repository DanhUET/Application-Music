package pro.branium.feature_recent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pro.branium.core_playback.usecase.GetCurrentPlaylistUseCase
import pro.branium.core_playback.usecase.GetPlayingSongUseCase
import pro.branium.core_ui.mapper.toDisplayModels
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.core_utils.MusicAppUtils
import pro.branium.feature_recent.domain.usecase.GetLimitedRecentSongUseCase
import pro.branium.feature_recent.domain.usecase.GetMoreRecentSongsUseCase
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    getLimitedRecentSongUseCase: GetLimitedRecentSongUseCase,
    getMoreRecentSongsUseCase: GetMoreRecentSongsUseCase,
    getPlayingSongUseCase: GetPlayingSongUseCase,
    getCurrentPlaylistUseCase: GetCurrentPlaylistUseCase,
) : ViewModel() {
    var playlist = MusicAppUtils.defaultPlaylists[3]!!

    val limitedRecentSongs: StateFlow<List<DisplaySongModel>> =
        getLimitedRecentSongUseCase(LIMIT)
            .map { songs ->
                playlist = playlist.copy(songs = songs)
                songs.toDisplayModels()
            }
            .combine(getPlayingSongUseCase()) { base, now ->
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
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val moreRecentSongs: StateFlow<List<DisplaySongModel>> =
        getMoreRecentSongsUseCase()
            .map { songs ->
                playlist = playlist.copy(songs = songs)
                songs.toDisplayModels()
            }
            .combine(getPlayingSongUseCase()) { base, now ->
                val currentId = now?.id
                val playing = now?.isPlaying ?: return@combine emptyList()
                val currentPlaylist = getCurrentPlaylistUseCase().value
                base.map { m ->
                    if (playlist.playlistId == currentPlaylist?.playlistId) {
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
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    companion object {
        const val LIMIT = 15
    }
}