package pro.branium.core_domain.repository

import pro.branium.core_model.PlaybackStateData

interface LastPlaybackStateRepository {
    fun loadLastPlaybackState(): PlaybackStateData

    fun saveLastPlaybackState(playbackStateData: PlaybackStateData)
}