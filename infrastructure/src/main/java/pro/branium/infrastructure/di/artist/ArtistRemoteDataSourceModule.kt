package pro.branium.infrastructure.di.artist

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_artist.data.datasource.ArtistRemoteDataSource
import pro.branium.infrastructure.source.artist.ArtistRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ArtistRemoteDataSourceModule {
    @Binds
    abstract fun bindArtistRemoteDataSource(
        remoteDataSource: ArtistRemoteDataSourceImpl
    ): ArtistRemoteDataSource
}