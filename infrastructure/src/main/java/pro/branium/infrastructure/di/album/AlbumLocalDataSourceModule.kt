package pro.branium.infrastructure.di.album

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_album.data.datasource.AlbumLocalDataSource
import pro.branium.infrastructure.source.album.AlbumLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumLocalDataSourceModule {
    @Binds
    abstract fun bindLocalAlbumDataSource(
        localDataSource: AlbumLocalDataSourceImpl
    ): AlbumLocalDataSource
}