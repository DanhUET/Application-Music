package pro.branium.musicapp.di.navigator_discovery

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.discovery.ForYouNavigator
import pro.branium.musicapp.navigator.discovery.AppForYouNavigator


@Module
@InstallIn(SingletonComponent::class)
abstract class ForYouNavigatorModule {
    @Binds
    abstract fun bindForYouNavigator(
        impl: AppForYouNavigator
    ): ForYouNavigator
}