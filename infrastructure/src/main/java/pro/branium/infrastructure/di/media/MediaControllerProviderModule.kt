package pro.branium.infrastructure.di.media

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_playback.MediaControllerProvider
import pro.branium.infrastructure.media.MediaControllerHolder

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaControllerProviderModule {
    @Binds
    abstract fun bindMediaControllerProvider(
        impl: MediaControllerHolder
    ): MediaControllerProvider
}