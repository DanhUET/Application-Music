package pro.branium.infrastructure.di.album

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_album.data.datasource.AlbumRemoteDataSource
import pro.branium.infrastructure.source.album.AlbumRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumRemoteDataSourceModule {
    @Binds
    abstract fun bindRemoteAlbumDataSource(
        remoteDataSource: AlbumRemoteDataSourceImpl
    ): AlbumRemoteDataSource
}