package pro.branium.musicapp.di.navigator_library

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.library.PlaylistDetailNavigator
import pro.branium.musicapp.navigator.library.AppPlaylistDetailNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistDetailNavigatorModule {
    @Binds
    abstract fun bindPlaylistDetailNavigator(
        impl: AppPlaylistDetailNavigator
    ): PlaylistDetailNavigator
}