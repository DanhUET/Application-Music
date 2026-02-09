package pro.branium.infrastructure.di.album

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_album.domain.repository.AlbumRepository
import pro.branium.infrastructure.repository.album.AlbumRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumRepositoryModule {
    @Binds
    abstract fun bindAlbumRepository(
        repository: AlbumRepositoryImpl
    ): AlbumRepository
}