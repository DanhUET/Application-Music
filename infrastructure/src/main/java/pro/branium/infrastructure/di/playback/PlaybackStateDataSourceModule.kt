package pro.branium.infrastructure.di.playback

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_playback.PlaybackStateDataSource
import pro.branium.infrastructure.source.playback.PlaybackStateDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaybackStateDataSourceModule {
    @Binds
    abstract fun bindPlaybackStateDataSource(
        impl: PlaybackStateDataSourceImpl
    ): PlaybackStateDataSource
}