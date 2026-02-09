package pro.branium.feature_foryou.viewmodel

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
import pro.branium.core_ui.mapper.toDisplayModel
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.core_utils.MusicAppUtils
import pro.branium.feature_foryou.domain.usecase.GetForYouSongsUseCase
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class ForYouViewModel @Inject constructor(
    getForYouSongsUseCase: GetForYouSongsUseCase,
    getPlayingSongUseCase: GetPlayingSongUseCase,
    getCurrentPlaylistUseCase: GetCurrentPlaylistUseCase
) : ViewModel() {
    var playlist = MusicAppUtils.defaultPlaylists[5]!!

    val top15ForYouSongs: StateFlow<List<DisplaySongModel>> =
        getForYouSongsUseCase(TOP_15_FOR_YOU_LIMIT).map { songs ->
            val displaySongs = songs.map { it.toDisplayModel() }
            playlist = playlist.copy(songs = songs)
            displaySongs
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
                SharingStarted.Companion.WhileSubscribed(5000),
                emptyList()
            )

    val top40ForYouSongs: StateFlow<List<DisplaySongModel>> =
        getForYouSongsUseCase(TOP_40_FOR_YOU_LIMIT).map { songs ->
            val displaySongs = songs.map { it.toDisplayModel() }
            playlist = playlist.copy(songs = songs)
            displaySongs
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
                SharingStarted.Companion.WhileSubscribed(5000),
                emptyList()
            )

    companion object {
        const val TOP_15_FOR_YOU_LIMIT = 15
        const val TOP_40_FOR_YOU_LIMIT = 40
    }
}