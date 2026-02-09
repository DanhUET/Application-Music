package pro.branium.musicapp.di.navigator_discovery

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.discovery.ArtistDetailNavigator
import pro.branium.musicapp.navigator.discovery.AppArtistDetailNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class ArtistDetailNavigatorModule {
    @Binds
    abstract fun bindArtistDetailNavigator(
        impl: AppArtistDetailNavigator
    ): ArtistDetailNavigator
}