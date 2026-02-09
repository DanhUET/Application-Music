package pro.branium.infrastructure.di.artist

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_artist.data.datasource.ArtistLocalDataSource
import pro.branium.infrastructure.source.artist.ArtistLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ArtistLocalDataSourceModule {
    @Binds
    abstract fun bindArtistLocalDataSource(
        localDataSource: ArtistLocalDataSourceImpl
    ): ArtistLocalDataSource
}