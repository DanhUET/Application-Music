package pro.branium.infrastructure.di.playlist

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_playlist.data.datasource.PlaylistLocalDataSource
import pro.branium.infrastructure.source.playlist.PlaylistLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistDataSourceModule {

    @Binds
    abstract fun bindLocalPlaylistDataSource(
        dataSource: PlaylistLocalDataSourceImpl
    ): PlaylistLocalDataSource
}