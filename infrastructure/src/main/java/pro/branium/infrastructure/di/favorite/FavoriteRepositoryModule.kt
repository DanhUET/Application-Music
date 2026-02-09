package pro.branium.infrastructure.di.favorite

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_domain.repository.FavoriteRepository
import pro.branium.infrastructure.repository.favorite.FavoriteRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteRepositoryModule {
    @Binds
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository
}