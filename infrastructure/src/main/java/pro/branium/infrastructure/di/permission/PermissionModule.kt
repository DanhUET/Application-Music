package pro.branium.infrastructure.di.permission

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_domain.repository.PermissionRepository
import pro.branium.infrastructure.repository.permission.PermissionRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PermissionModule {

    @Singleton
    @Binds
    abstract fun bindPermissionRepository(
        impl: PermissionRepositoryImpl
    ): PermissionRepository
}
