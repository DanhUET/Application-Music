package pro.branium.musicapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import pro.branium.core_navigation.navigator.PlayerNavigator
import pro.branium.musicapp.navigator.PlayerNavigatorImpl

@Module
@InstallIn(ActivityComponent::class)
object PlayerNavigatorModule {
    @Provides
    fun providePlayerNavigator(
        @ActivityContext context: Context
    ): PlayerNavigator {
        return PlayerNavigatorImpl(context)
    }
}