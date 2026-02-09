package pro.branium.musicapp.di.navigator_home

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.home.AlbumDetailNavigator
import pro.branium.musicapp.navigator.home.AppAlbumDetailNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumDetailNavigatorModule {
    @Binds
    abstract fun bindAlbumDetailNavigator(
        impl: AppAlbumDetailNavigator
    ): AlbumDetailNavigator
}