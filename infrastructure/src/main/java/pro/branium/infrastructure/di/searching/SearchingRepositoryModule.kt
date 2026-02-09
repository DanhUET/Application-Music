package pro.branium.infrastructure.di.searching

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature.search.domain.repository.SongSearchingRepository
import pro.branium.infrastructure.repository.searching.SongSearchingRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchingRepositoryModule {
    @Binds
    abstract fun bindSearchingRepository(
        dataSource: SongSearchingRepositoryImpl
    ): SongSearchingRepository
}