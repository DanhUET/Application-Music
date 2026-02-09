package pro.branium.infrastructure.di.song

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_song.data.datasource.SongRemoteDataSource
import pro.branium.infrastructure.source.song.SongRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SongRemoteDataSourceModule {
    @Binds
    abstract fun bindSongRemoteDataSource(
        remoteDataSource: SongRemoteDataSourceImpl
    ): SongRemoteDataSource
}