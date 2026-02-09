package pro.branium.infrastructure.di.core

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pro.branium.core_database.dao.album.AlbumDao
import pro.branium.core_database.dao.album.AlbumRemoteKeysDao
import pro.branium.core_database.dao.album.AlbumSongCrossRefDao
import pro.branium.feature_artist.data.dao.ArtistDao
import pro.branium.feature_artist.data.dao.ArtistRemoteKeysDao
import pro.branium.feature_artist.data.dao.ArtistSongCrossRefDao
import pro.branium.feature_favorite.data.dao.FavoriteSongDao
import pro.branium.core_database.dao.playlist.PlaylistDao
import pro.branium.core_database.dao.playlist.PlaylistSongCrossRefDao
import pro.branium.feature.search.data.dao.HistorySearchedKeyDao
import pro.branium.feature.search.data.dao.SearchingDao
import pro.branium.core_database.dao.song.SongDao
import pro.branium.core_database.dao.song.SongRemoteKeysDao
import pro.branium.infrastructure.db.dao.tracking.DBTrackingDao
import pro.branium.core_database.dao.user.UserDao
import pro.branium.core_database.dao.user.UserFavoriteDao
import pro.branium.core_database.dao.user.UserSearchedKeyCrossRefDao
import pro.branium.core_database.dao.user.UserSearchedSongCrossRefDao
import pro.branium.core_database.dao.user.UserSongCrossRefDao
import pro.branium.infrastructure.db.AppDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    @Suppress("deprecation")
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "music.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideSongDao(appDatabase: AppDatabase): SongDao {
        return appDatabase.songDao()
    }

    @Provides
    fun providePlaylistDao(appDatabase: AppDatabase): PlaylistDao {
        return appDatabase.playlistDao()
    }

    @Provides
    fun provideAlbumDao(appDatabase: AppDatabase): AlbumDao {
        return appDatabase.albumDao()
    }

    @Provides
    fun provideArtistDao(appDatabase: AppDatabase): ArtistDao {
        return appDatabase.artistDao()
    }

    @Provides
    fun provideSongRemoteKeysDao(appDatabase: AppDatabase): SongRemoteKeysDao {
        return appDatabase.songRemoteKeysDao()
    }

    @Provides
    fun provideDbTrackingDao(appDatabase: AppDatabase): DBTrackingDao {
        return appDatabase.dbTrackingDao()
    }

    @Provides
    fun provideArtistRemoteKeysDao(appDatabase: AppDatabase): ArtistRemoteKeysDao {
        return appDatabase.artistRemoteKeysDao()
    }

    @Provides
    fun provideAlbumRemoteKeysDao(appDatabase: AppDatabase): AlbumRemoteKeysDao {
        return appDatabase.albumRemoteKeysDao()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun providePlaylistSongCrossRefDao(appDatabase: AppDatabase): PlaylistSongCrossRefDao {
        return appDatabase.playlistSongCrossRefDao()
    }

    @Provides
    fun provideAlbumSongCrossRefDao(appDatabase: AppDatabase): AlbumSongCrossRefDao {
        return appDatabase.albumSongCrossRefDao()
    }

    @Provides
    fun provideArtistSongCrossRefDao(appDatabase: AppDatabase): ArtistSongCrossRefDao {
        return appDatabase.artistSongCrossRefDao()
    }

    @Provides
    fun provideUserSongCrossRefDao(appDatabase: AppDatabase): UserSongCrossRefDao {
        return appDatabase.userSongCrossRefDao()
    }

    @Provides
    fun provideUserSearchedSongCrossRefDao(appDatabase: AppDatabase): UserSearchedSongCrossRefDao {
        return appDatabase.userSearchedSongCrossRefDao()
    }

    @Provides
    fun provideUserSearchedKeyCrossRefDao(appDatabase: AppDatabase): UserSearchedKeyCrossRefDao {
        return appDatabase.userSearchedKeyCrossRefDao()
    }

    @Provides
    fun provideHistorySearchedKeyDao(appDatabase: AppDatabase): HistorySearchedKeyDao {
        return appDatabase.historySearchedKeyDao()
    }

    @Provides
    fun provideSearchingDao(appDatabase: AppDatabase): SearchingDao {
        return appDatabase.searchingDao()
    }

    @Provides
    fun provideUserFavoriteDao(appDatabase: AppDatabase): UserFavoriteDao {
        return appDatabase.userFavoriteDao()
    }

    @Provides
    fun provideFavoriteSongDao(appDatabase: AppDatabase): FavoriteSongDao {
        return appDatabase.favoriteSongDao()
    }
}
