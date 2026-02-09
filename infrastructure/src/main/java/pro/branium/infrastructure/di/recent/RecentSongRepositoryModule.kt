package pro.branium.infrastructure.di.recent

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_domain.repository.RecentSongRepository
import pro.branium.infrastructure.repository.recent.RecentSongRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RecentSongRepositoryModule {
    @Binds
    abstract fun bindRecentSongRepository(
        dataSource: RecentSongRepositoryImpl
    ): RecentSongRepository
}