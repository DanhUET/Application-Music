package pro.branium.infrastructure.di.recent

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_recent.data.datasource.RecentSongRemoteDataSource
import pro.branium.infrastructure.source.recent.RecentSongRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RecentSongRemoteDataSourceModule {
    @Binds
    abstract fun bindRecentSongRemoteDataSource(
        impl: RecentSongRemoteDataSourceImpl
    ): RecentSongRemoteDataSource
}