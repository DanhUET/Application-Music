package pro.branium.infrastructure.di.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_user.data.datasource.UserRemoteDataSource
import pro.branium.infrastructure.source.user.UserRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRemoteDataSourceModule {
    @Binds
    abstract fun bindUserRemoteDataSource(impl: UserRemoteDataSourceImpl): UserRemoteDataSource
}