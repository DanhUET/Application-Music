package pro.branium.infrastructure.di.song

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_song.data.datasource.SongLocalDataSource
import pro.branium.infrastructure.source.song.SongLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SongLocalDataSourceModule {
    @Binds
    abstract fun bindSongLocalDataSource(
        localDataSource: SongLocalDataSourceImpl
    ): SongLocalDataSource
}