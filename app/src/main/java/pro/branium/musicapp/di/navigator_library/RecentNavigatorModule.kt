package pro.branium.musicapp.di.navigator_library

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.library.RecentNavigator
import pro.branium.musicapp.navigator.library.AppRecentNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class RecentNavigatorModule {
    @Binds
    abstract fun bindPlaylistDetailNavigator(
        impl: AppRecentNavigator
    ): RecentNavigator
}