package pro.branium.infrastructure.repository.playback

import pro.branium.core_model.PlaybackStateData
import pro.branium.core_domain.repository.LastPlaybackStateRepository
import pro.branium.core_playback.PlaybackStateDataSource
import javax.inject.Inject

class LastPlaybackStateRepositoryImpl @Inject constructor(
    private val dataSource: PlaybackStateDataSource
) : LastPlaybackStateRepository {
    override fun loadLastPlaybackState(): PlaybackStateData {
        return dataSource.loadLastPlaybackState()
    }

    override fun saveLastPlaybackState(playbackStateData: PlaybackStateData) {
        dataSource.saveLastPlaybackState(playbackStateData)
    }
}