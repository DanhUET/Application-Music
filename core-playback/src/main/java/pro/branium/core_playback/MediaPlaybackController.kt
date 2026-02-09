package pro.branium.core_playback

import androidx.media3.common.MediaItem

interface MediaPlaybackController {
    suspend fun setPlaylist(mediaItems: List<MediaItem>, startIndex: Int = 0)
    fun hasNextMedia(): Boolean
    fun hasPreviousMedia(): Boolean
    suspend fun play(songId: String? = null)
    fun pause()
    fun seekTo(position: Long)
    fun seekToNext()
    fun seekToPrevious()
    fun getCurrentPlayingSongId(): String?
    fun getCurrentPlayingSongIndex(): Int
    fun toggleShuffleMode()
    fun changeRepeatMode()
}