package pro.branium.infrastructure.di.searching

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature.search.domain.repository.HistorySearchedKeyRepository
import pro.branium.infrastructure.repository.searching.HistorySearchedKeyRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class HistorySearchedKeyRepositoryModule {
    @Binds
    abstract fun bindHistorySearchedKeyRepository(
        impl: HistorySearchedKeyRepositoryImpl
    ): HistorySearchedKeyRepository
}