package pro.branium.musicapp.di.navigator_home

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.home.RecommendedNavigator
import pro.branium.musicapp.navigator.home.AppRecommendedNavigator

@Module
@InstallIn(SingletonComponent::class)
abstract class RecommendedNavigatorModule {
    @Binds
    abstract fun bindRecommendedNavigator(
        impl: AppRecommendedNavigator
    ): RecommendedNavigator
}