package pro.branium.infrastructure.di.media

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_playback.MediaPlaybackController
import pro.branium.infrastructure.media.MediaPlaybackControllerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaPlaybackControllerModule {
    @Binds
    abstract fun bindMediaPlaybackController(
        impl: MediaPlaybackControllerImpl
    ): MediaPlaybackController
}