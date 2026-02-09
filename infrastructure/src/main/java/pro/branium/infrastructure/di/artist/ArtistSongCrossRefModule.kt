package pro.branium.infrastructure.di.artist

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_artist.data.datasource.ArtistSongCrossRefDataSource
import pro.branium.infrastructure.source.artist.ArtistSongCrossRefDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ArtistSongCrossRefModule {
    @Binds
    abstract fun bindArtistSongCrossRefDataSource(
        dataSource: ArtistSongCrossRefDataSourceImpl
    ): ArtistSongCrossRefDataSource
}