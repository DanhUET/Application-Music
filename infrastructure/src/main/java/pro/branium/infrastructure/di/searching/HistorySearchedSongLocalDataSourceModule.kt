package pro.branium.infrastructure.di.searching

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature.search.data.datasource.HistorySearchedSongLocalDataSource
import pro.branium.infrastructure.source.searching.HistorySearchedSongLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class HistorySearchedSongLocalDataSourceModule {
    @Binds
    abstract fun bindHistorySearchedSongLocalDataSource(
        impl: HistorySearchedSongLocalDataSourceImpl
    ): HistorySearchedSongLocalDataSource
}