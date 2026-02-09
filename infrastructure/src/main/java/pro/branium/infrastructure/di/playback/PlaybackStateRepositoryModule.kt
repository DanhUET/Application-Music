package pro.branium.infrastructure.di.playback

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_domain.repository.PlaybackStateRepository
import pro.branium.infrastructure.repository.playback.PlaybackStateRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaybackStateRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPlaybackRepository(
        impl: PlaybackStateRepositoryImpl
    ): PlaybackStateRepository
}