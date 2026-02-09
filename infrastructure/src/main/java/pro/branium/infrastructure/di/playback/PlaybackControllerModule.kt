package pro.branium.infrastructure.di.playback

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_playback.PlaybackController
import pro.branium.infrastructure.playback.PlaybackControllerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaybackControllerModule {
    @Binds
    abstract fun bindPlaybackController(
        playbackControllerImpl: PlaybackControllerImpl
    ): PlaybackController
}
