package pro.branium.musicapp.di.navigator_library

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.library.FavoriteSongNavigator
import pro.branium.musicapp.navigator.library.AppFavoriteSongNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteSongNavigatorModule {
    @Binds
    abstract fun bindFavoriteSongNavigator(
        impl: AppFavoriteSongNavigator
    ): FavoriteSongNavigator
}