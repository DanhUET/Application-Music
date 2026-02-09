package pro.branium.musicapp.di.navigator_discovery

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.discovery.MostListenedNavigator
import pro.branium.musicapp.navigator.discovery.AppMostListenedNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class MostListenedNavigatorModule {
    @Binds
    abstract fun bindMostListenedNavigator(
        impl: AppMostListenedNavigator
    ): MostListenedNavigator
}
