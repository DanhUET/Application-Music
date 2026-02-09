package pro.branium.infrastructure.di.navigator

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.navigator.SongOptionMenuNavigator
import pro.branium.infrastructure.navigator.SongOptionMenuNavigatorImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SongOptionMenuNavigatorModule {
    @Binds
    abstract fun bindSongOptionMenuNavigator(
        impl: SongOptionMenuNavigatorImpl
    ): SongOptionMenuNavigator
}