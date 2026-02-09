package pro.branium.infrastructure.di.searching

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature.search.data.datasource.HistorySearchedKeyLocalDataSource
import pro.branium.infrastructure.source.searching.HistorySearchedKeyLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class HistorySearchedKeyLocalDataSourceModule {
    @Binds
    abstract fun bindHistorySearchedKeyLocalDataSource(
        impl: HistorySearchedKeyLocalDataSourceImpl
    ): HistorySearchedKeyLocalDataSource
}