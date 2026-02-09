package pro.branium.infrastructure.di.song

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_song.domain.repository.BlockRepository
import pro.branium.infrastructure.repository.song.BlockRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class BlockRepositoryModule {
    @Binds
    abstract fun bindBlockRepository(impl: BlockRepositoryImpl): BlockRepository
}