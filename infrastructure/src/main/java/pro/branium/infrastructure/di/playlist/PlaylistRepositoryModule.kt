package pro.branium.infrastructure.di.playlist

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_domain.playlist.repository.PlaylistRepository
import pro.branium.infrastructure.repository.playlist.PlaylistRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistRepositoryModule {
    @Binds
    abstract fun bindPlaylistRepository(
        repository: PlaylistRepositoryImpl
    ): PlaylistRepository
}