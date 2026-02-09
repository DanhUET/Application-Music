package pro.branium.infrastructure.di.navigator

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.navigator.PlaylistNavigator
import pro.branium.infrastructure.navigator.PlaylistNavigatorImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistNavigatorModule {
    @Binds
    abstract fun bindPlaylistNavigator(impl: PlaylistNavigatorImpl): PlaylistNavigator
}