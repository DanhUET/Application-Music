package pro.branium.infrastructure.di.recent

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_recent.data.datasource.RecentSongLocalDataSource
import pro.branium.infrastructure.source.recent.RecentSongLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RecentSongLocalDataSourceModule {
    @Binds
    abstract fun bindRecentSongLocalDataSource(
        dataSource: RecentSongLocalDataSourceImpl
    ): RecentSongLocalDataSource
}