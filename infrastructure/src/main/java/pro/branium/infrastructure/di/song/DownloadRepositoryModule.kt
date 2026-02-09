package pro.branium.infrastructure.di.song

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_song.domain.repository.DownloadRepository
import pro.branium.infrastructure.repository.song.DownloadRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadRepositoryModule {
    @Binds
    abstract fun bindDownloadRepository(
        downloadRepositoryImpl: DownloadRepositoryImpl
    ): DownloadRepository
}