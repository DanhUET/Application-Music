package pro.branium.infrastructure.di.playback

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_domain.repository.LastPlaybackStateRepository
import pro.branium.infrastructure.repository.playback.LastPlaybackStateRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class LastPlaybackStateRepositoryModule {
    @Binds
    abstract fun bindPlaybackStateRepository(
        impl: LastPlaybackStateRepositoryImpl
    ): LastPlaybackStateRepository
}