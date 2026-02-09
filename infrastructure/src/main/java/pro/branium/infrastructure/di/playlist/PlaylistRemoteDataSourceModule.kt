package pro.branium.infrastructure.di.playlist

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_playlist.data.datasource.PlaylistRemoteDataSource
import pro.branium.infrastructure.source.playlist.PlaylistRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistRemoteDataSourceModule {
    @Binds
    abstract fun bindPlaylistRemoteDataSource(
        impl: PlaylistRemoteDataSourceImpl
    ): PlaylistRemoteDataSource
}