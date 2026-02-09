package pro.branium.infrastructure.di.song

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_song.domain.repository.ReportRepository
import pro.branium.infrastructure.repository.song.ReportErrorRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ReportErrorRepositoryModule {
    @Binds
    abstract fun bindReportErrorRepository(
        reportErrorRepositoryImpl: ReportErrorRepositoryImpl
    ): ReportRepository
}