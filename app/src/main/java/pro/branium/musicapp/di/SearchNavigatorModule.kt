package pro.branium.musicapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import pro.branium.core_navigation.navigator.SearchNavigator
import pro.branium.musicapp.navigator.AppSearchNavigator

@Module
@InstallIn(ActivityComponent::class)
object SearchNavigatorModule {
    @Provides
    fun provideAppSearchNavigator(
        @ActivityContext context: Context
    ): SearchNavigator {
        return AppSearchNavigator(context)
    }
}