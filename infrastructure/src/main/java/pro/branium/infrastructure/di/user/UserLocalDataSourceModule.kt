package pro.branium.infrastructure.di.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.feature_user.data.datasource.UserLocalDataSource
import pro.branium.infrastructure.source.user.UserLocalDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UserLocalDataSourceModule {
    @Binds
    abstract fun bindUserLocalDataSource(impl: UserLocalDataSourceImpl): UserLocalDataSource
}