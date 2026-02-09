package pro.branium.infrastructure.di.artist

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_artist.domain.repository.ArtistRepository
import pro.branium.infrastructure.repository.artist.ArtistRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ArtistRepositoryModule {
    @Binds
    abstract fun bindArtistRepository(
        repository: ArtistRepositoryImpl
    ): ArtistRepository
}