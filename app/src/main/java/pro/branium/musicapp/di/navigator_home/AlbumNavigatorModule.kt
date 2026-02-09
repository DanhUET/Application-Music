package pro.branium.musicapp.di.navigator_home

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.home.AlbumNavigator
import pro.branium.musicapp.navigator.home.AppAlbumNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumNavigatorModule {
    @Binds
    abstract fun bindAlbumNavigator(
        impl: AppAlbumNavigator
    ): AlbumNavigator
}