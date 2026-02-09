package pro.branium.infrastructure.di.favorite

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_favorite.data.datasource.FavoriteLocalDataSource
import pro.branium.infrastructure.source.favorite.FavoriteLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteLocalDataSourceModule {
    @Binds
    abstract fun bindFavoriteLocalDataSource(
        impl: FavoriteLocalDataSourceImpl
    ): FavoriteLocalDataSource
}