package pro.branium.infrastructure.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import pro.branium.core_database.dao.album.AlbumDao
import pro.branium.core_database.dao.album.AlbumRemoteKeysDao
import pro.branium.core_database.dao.album.AlbumSongCrossRefDao
import pro.branium.core_database.dao.playlist.PlaylistDao
import pro.branium.core_database.dao.playlist.PlaylistSongCrossRefDao
import pro.branium.core_database.dao.song.SongDao
import pro.branium.core_database.dao.song.SongRemoteKeysDao
import pro.branium.core_database.dao.user.UserDao
import pro.branium.core_database.dao.user.UserFavoriteDao
import pro.branium.core_database.dao.user.UserSearchedKeyCrossRefDao
import pro.branium.core_database.dao.user.UserSearchedSongCrossRefDao
import pro.branium.core_database.dao.user.UserSongCrossRefDao
import pro.branium.core_database.entity.album.AlbumEntity
import pro.branium.core_database.entity.album.AlbumSongCrossRefEntity
import pro.branium.core_database.entity.paging.AlbumRemoteKeysEntity
import pro.branium.core_database.entity.paging.ArtistRemoteKeysEntity
import pro.branium.core_database.entity.paging.SongRemoteKeysEntity
import pro.branium.core_database.entity.playlist.PlaylistEntity
import pro.branium.core_database.entity.playlist.PlaylistSongCrossRefEntity
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.core_database.entity.user.UserEntity
import pro.branium.core_database.entity.user.UserFavoriteSongCrossRefEntity
import pro.branium.core_database.entity.user.UserSearchedKeyCrossRefEntity
import pro.branium.core_database.entity.user.UserSearchedSongCrossRefEntity
import pro.branium.core_database.entity.user.UserSongCrossRefEntity
import pro.branium.feature.search.data.dao.HistorySearchedKeyDao
import pro.branium.feature.search.data.dao.SearchingDao
import pro.branium.feature.search.data.entity.HistorySearchedKeyEntity
import pro.branium.feature_artist.data.dao.ArtistDao
import pro.branium.feature_artist.data.dao.ArtistRemoteKeysDao
import pro.branium.feature_artist.data.dao.ArtistSongCrossRefDao
import pro.branium.feature_artist.data.entity.ArtistEntity
import pro.branium.feature_artist.data.entity.ArtistSongCrossRefEntity
import pro.branium.feature_favorite.data.dao.FavoriteSongDao
import pro.branium.feature_favorite.data.entity.FavoriteEntity
import pro.branium.infrastructure.db.dao.tracking.DBTrackingDao
import pro.branium.infrastructure.entity.tracking.DBTrackingEntity

@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        AlbumEntity::class,
        PlaylistSongCrossRefEntity::class,
        ArtistEntity::class,
        ArtistSongCrossRefEntity::class,
        SongRemoteKeysEntity::class,
        DBTrackingEntity::class,
        ArtistRemoteKeysEntity::class,
        AlbumRemoteKeysEntity::class,
        HistorySearchedKeyEntity::class,
        AlbumSongCrossRefEntity::class,
        UserSongCrossRefEntity::class,
        FavoriteEntity::class,
        UserEntity::class,
        UserSearchedSongCrossRefEntity::class,
        UserSearchedKeyCrossRefEntity::class,
        UserFavoriteSongCrossRefEntity::class
    ],
    version = 10,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 9,
            to = 10,
            spec = AppDatabase.MigrationSpec::class
        )
    ]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
    abstract fun songRemoteKeysDao(): SongRemoteKeysDao
    abstract fun dbTrackingDao(): DBTrackingDao
    abstract fun artistRemoteKeysDao(): ArtistRemoteKeysDao
    abstract fun albumRemoteKeysDao(): AlbumRemoteKeysDao
    abstract fun userDao(): UserDao
    abstract fun playlistSongCrossRefDao(): PlaylistSongCrossRefDao
    abstract fun artistSongCrossRefDao(): ArtistSongCrossRefDao
    abstract fun albumSongCrossRefDao(): AlbumSongCrossRefDao
    abstract fun userSongCrossRefDao(): UserSongCrossRefDao
    abstract fun userSearchedSongCrossRefDao(): UserSearchedSongCrossRefDao
    abstract fun userSearchedKeyCrossRefDao(): UserSearchedKeyCrossRefDao
    abstract fun historySearchedKeyDao(): HistorySearchedKeyDao
    abstract fun searchingDao(): SearchingDao
    abstract fun userFavoriteDao(): UserFavoriteDao
    abstract fun favoriteSongDao(): FavoriteSongDao

    @DeleteColumn.Entries(
        DeleteColumn(
            tableName = "songs",
            columnName = "favorite"
        ),
        DeleteColumn(
            tableName = "albums",
            columnName = "size"
        ),
        DeleteColumn(
            tableName = "artists",
            columnName = "care_about"
        )
    )
    @RenameColumn.Entries(
        RenameColumn(
            tableName = "albums",
            fromColumnName = "artwork",
            toColumnName = "artwork_url"
        )
    )
    internal class MigrationSpec : AutoMigrationSpec
}