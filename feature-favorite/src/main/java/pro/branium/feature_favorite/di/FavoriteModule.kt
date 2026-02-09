package pro.branium.feature_favorite.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_domain.manager.FavoriteManager
import pro.branium.feature_favorite.manager.FavoriteManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteModule {
    @Binds
    @Singleton
    abstract fun bindFavoriteManager(impl: FavoriteManagerImpl): FavoriteManager
}