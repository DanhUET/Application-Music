package pro.branium.infrastructure.di.searching

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature.search.data.datasource.SearchingLocalDataSource
import pro.branium.infrastructure.source.searching.SearchingLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchingDataSourceModule {
    @Binds
    abstract fun bindSearchingDataSource(
        dataSource: SearchingLocalDataSourceImpl
    ): SearchingLocalDataSource
}