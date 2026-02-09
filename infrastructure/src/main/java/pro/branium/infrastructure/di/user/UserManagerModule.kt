package pro.branium.infrastructure.di.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.infrastructure.repository.user.DefaultUserManager
import pro.branium.core_domain.manager.UserManager

@Module
@InstallIn(SingletonComponent::class)
abstract class UserManagerModule {
    @Binds
    abstract fun bindUserManager(userManager: DefaultUserManager): UserManager
}