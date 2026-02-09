package pro.branium.musicapp.di.navigator_discovery

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.discovery.ArtistNavigator
import pro.branium.musicapp.navigator.discovery.AppArtistNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class ArtistNavigatorModule {
    @Binds
    abstract fun bindArtistNavigator(
        impl: AppArtistNavigator
    ): ArtistNavigator
}