package pro.branium.core_domain.repository

import kotlinx.coroutines.flow.StateFlow
import pro.branium.core_model.NowPlaying
import pro.branium.core_model.PlaylistModel

interface PlaybackStateRepository {
    /** Bài hát đang phát */
    val nowPlaying: StateFlow<NowPlaying?>

    /** Vị trí hiện tại của bài nhạc */
    val currentPosition: StateFlow<Long>

    /** Trạng thái trộn phát nhạc */
    val shuffleMode: StateFlow<Boolean>

    /** Trạng thái lặp lại bài nhạc */
    val repeatMode: StateFlow<RepeatMode>

    /** Playlist hiện tại */
    val currentPlaylist: StateFlow<PlaylistModel?>

    /** Những thay đổi của trạng thái phát **/
    val playbackStateChanged: StateFlow<Int>

    /** Update playlist hiện tại */
    fun updateCurrentPlaylist(playlist: PlaylistModel?)
}

enum class RepeatMode { NONE, ONE, ALL }