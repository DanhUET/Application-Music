package pro.branium.musicapp.di.navigator_library

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.library.PlaylistNavigator
import pro.branium.musicapp.navigator.library.AppPlaylistNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistNavigatorModule {
    @Binds
    abstract fun bindPlaylistDetailNavigator(
        impl: AppPlaylistNavigator
    ): PlaylistNavigator
}