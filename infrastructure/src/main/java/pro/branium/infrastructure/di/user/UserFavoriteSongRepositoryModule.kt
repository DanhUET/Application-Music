package pro.branium.infrastructure.di.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_user.domain.repository.UserFavoriteSongRepository
import pro.branium.infrastructure.repository.user.UserFavoriteSongRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UserFavoriteSongRepositoryModule {
    @Binds
    abstract fun bindUserFavoriteSongRepository(
        impl: UserFavoriteSongRepositoryImpl
    ): UserFavoriteSongRepository
}