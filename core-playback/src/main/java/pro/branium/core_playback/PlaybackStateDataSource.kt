package pro.branium.core_playback

import pro.branium.core_model.PlaybackStateData

interface PlaybackStateDataSource {
    fun loadLastPlaybackState(): PlaybackStateData

    fun saveLastPlaybackState(playbackStateData: PlaybackStateData)
}