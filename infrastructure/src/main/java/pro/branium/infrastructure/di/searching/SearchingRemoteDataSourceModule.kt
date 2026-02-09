package pro.branium.infrastructure.di.searching

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature.search.data.datasource.SearchingRemoteDataSource
import pro.branium.infrastructure.source.searching.SearchingRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchingRemoteDataSourceModule {
    @Binds
    abstract fun bindSearchingRemoteDataSource(
        searchingRemoteDataSourceImpl: SearchingRemoteDataSourceImpl
    ): SearchingRemoteDataSource
}