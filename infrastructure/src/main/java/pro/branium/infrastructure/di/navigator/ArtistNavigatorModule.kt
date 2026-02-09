package pro.branium.infrastructure.di.navigator

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.navigator.ArtistNavigator
import pro.branium.infrastructure.navigator.ArtistNavigatorImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ArtistNavigatorModule {
    @Binds
    abstract fun bindNavigator(navigator: ArtistNavigatorImpl): ArtistNavigator
}