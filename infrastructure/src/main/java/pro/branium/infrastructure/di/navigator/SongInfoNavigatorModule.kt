package pro.branium.infrastructure.di.navigator

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_navigation.navigator.SongInfoNavigator
import pro.branium.infrastructure.navigator.SongInfoNavigatorImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SongInfoNavigatorModule {
    @Binds
    abstract fun bindSongInfoNavigator(impl: SongInfoNavigatorImpl): SongInfoNavigator
}