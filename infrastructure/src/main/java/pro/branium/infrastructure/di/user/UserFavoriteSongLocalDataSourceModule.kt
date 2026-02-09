package pro.branium.infrastructure.di.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_user.data.datasource.UserFavoriteSongLocalDataSource
import pro.branium.infrastructure.source.user.UserFavoriteSongLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UserFavoriteSongLocalDataSourceModule {
    @Binds
    abstract fun bindUserFavoriteSongLocalDataSource(
        impl: UserFavoriteSongLocalDataSourceImpl
    ): UserFavoriteSongLocalDataSource
}