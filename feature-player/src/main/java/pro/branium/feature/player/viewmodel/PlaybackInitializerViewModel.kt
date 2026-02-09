package pro.branium.feature.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import pro.branium.core_domain.usecase.GetMostRecentSongUseCase
import pro.branium.core_model.PlaybackStateData
import pro.branium.core_model.PlaylistModel
import pro.branium.core_model.SongModel
import pro.branium.core_playback.usecase.LoadPreviousSessionSongUseCase
import pro.branium.core_playback.usecase.SetCurrentPlaylistUseCase
import pro.branium.core_ui.mapper.toDisplayModel
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.core_utils.MusicAppUtils
import pro.branium.feature.player.domain.usecase.GetSongsInPlaylistUseCase
import javax.inject.Inject

@HiltViewModel
class PlaybackInitializerViewModel @Inject constructor(
    private val loadPreviousSessionSongUseCase: LoadPreviousSessionSongUseCase,
    private val getSongsInPlaylistUseCase: GetSongsInPlaylistUseCase,
    private val setCurrentPlaylistUseCase: SetCurrentPlaylistUseCase,
    private val getMostRecentSongUseCase: GetMostRecentSongUseCase
) : ViewModel() {

    private val _nowPlayingSong = MutableStateFlow<DisplaySongModel?>(null)
    val nowPlayingSong: StateFlow<DisplaySongModel?> = _nowPlayingSong

    var shouldAutoPlay: Boolean = false

    fun loadPreviousSessionSong(playbackState: PlaybackStateData) {
        viewModelScope.launch(Dispatchers.IO) {
            shouldAutoPlay = false
            val songId = playbackState.songId

            // Lấy bài hát từ session trước hoặc recent
            val lastSessionSong = songId?.let {
                loadPreviousSessionSongUseCase(it, playbackState.playlistId)
            } ?: getMostRecentSongUseCase().firstOrNull()

            // Lấy playlist nếu có
            var playlist = playbackState.playlistId?.let { playlistId ->
                MusicAppUtils.defaultPlaylists[playlistId]?.copy(
                    songs = getSongsInPlaylistUseCase(playlistId)
                )
            }

            // Nếu không có playlist nhưng có bài hát → tạo playlist mặc định
            if (playlist == null && lastSessionSong != null) {
                playlist = createDefaultPlaylist(lastSessionSong)
            }

            // Cập nhật current playlist và nowPlayingSong
            playlist?.let { p ->
                val startIndex = lastSessionSong?.let { song ->
                    p.songs.indexOfFirst { it.id == song.id }
                } ?: -1

                setCurrentPlaylistUseCase(p, startIndex)

                _nowPlayingSong.value = lastSessionSong?.toDisplayModel(
                    isFavorite = false,
                    playingPosition = playbackState.position.takeIf { songId != null } ?: 0
                )
            }
        }
    }

    private fun createDefaultPlaylist(song: SongModel): PlaylistModel =
        PlaylistModel(playlistId = System.currentTimeMillis(), songs = listOf(song))
}
