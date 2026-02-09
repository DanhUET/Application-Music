package pro.branium.infrastructure.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.SessionToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pro.branium.infrastructure.media.PlaybackService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Media3Module {
    @Provides
    @Singleton
    fun provideSessionToken(
        @ApplicationContext context: Context
    ): SessionToken {
        return SessionToken(
            context,
            ComponentName(context, PlaybackService::class.java)
        )
    }
}