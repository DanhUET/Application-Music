package pro.branium.infrastructure.di.navigator

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.navigator.AlbumNavigator
import pro.branium.infrastructure.navigator.AlbumNavigatorImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumNavigatorModule {
    @Binds
    abstract fun bindsAlbumNavigator(impl: AlbumNavigatorImpl): AlbumNavigator
}