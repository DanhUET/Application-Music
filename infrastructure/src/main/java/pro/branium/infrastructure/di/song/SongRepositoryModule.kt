package pro.branium.infrastructure.di.song

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_domain.repository.SongRepository
import pro.branium.infrastructure.repository.song.SongRepositoryImpl

@InstallIn(SingletonComponent::class)
@Module
abstract class SongRepositoryModule {
    @Binds
    abstract fun bindSongRepository(repository: SongRepositoryImpl): SongRepository
}