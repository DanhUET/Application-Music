package pro.branium.infrastructure.di.favorite

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_favorite.data.datasource.FavoriteRemoteDataSource
import pro.branium.infrastructure.source.favorite.FavoriteRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteRemoteDataSourceModule {
    @Binds
    abstract fun bindFavoriteRemoteDataSource(
        impl: FavoriteRemoteDataSourceImpl
    ): FavoriteRemoteDataSource
}