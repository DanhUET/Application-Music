package pro.branium.infrastructure.di.core

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pro.branium.core_network.NetworkMonitor
import pro.branium.feature_artist.data.api.ArtistApi
import pro.branium.feature_song.data.SongApi
import pro.branium.infrastructure.network.NetworkMonitorImpl
import pro.branium.infrastructure.source.album.AlbumApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://thantrieu.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideSongApi(retrofit: Retrofit): SongApi {
        return retrofit.create(SongApi::class.java)
    }

    @Provides
    fun provideAlbumApi(retrofit: Retrofit): AlbumApi {
        return retrofit.create(AlbumApi::class.java)
    }

    @Provides
    fun provideArtistApi(retrofit: Retrofit): ArtistApi {
        return retrofit.create(ArtistApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor {
        return NetworkMonitorImpl(context)
    }
}