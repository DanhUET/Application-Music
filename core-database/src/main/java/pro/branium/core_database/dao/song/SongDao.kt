package pro.branium.core_database.dao.song

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity

@Dao
interface SongDao {
    @Query(
        "SELECT song_id, title, album, artist, source, image, " +
                "duration, counter, track_number, genre, year, lyrics_url FROM songs"
    )
    fun songPagingSource(): PagingSource<Int, SongEntity>

    @Query(
        "SELECT song_id, title, album, artist, source, image, duration, " +
                "counter, track_number, genre, year, lyrics_url FROM songs LIMIT :limit"
    )
    fun getNSongPagingSource(limit: Int): PagingSource<Int, SongEntity>

    @Query(
        "SELECT song_id, title, album, artist, source, image, " +
                "duration, counter, track_number, genre, year, lyrics_url " +
                "FROM songs WHERE album LIKE :album"
    )
    suspend fun getSongsInAlbum(album: String): List<SongEntity>

    @Query(
        "SELECT song_id, title, album, artist, source, image, " +
                "duration, counter, track_number, genre, year, lyrics_url " +
                "FROM songs " +
                "ORDER BY track_number ASC " +
                "LIMIT :limit"
    )
    suspend fun getLimitedSongs(limit: Int): List<SongEntity>

    @Query(
        "SELECT song_id, title, album, artist, source, image, " +
                "duration, counter, track_number, genre, year, lyrics_url " +
                "FROM songs " +
                "ORDER BY track_number ASC"
    )
    suspend fun getAllSongs(): List<SongEntity>

    @Query(
        "SELECT s.song_id, s.title, s.album, s.artist, s.source, s.image, " +
                "s.duration, s.counter, s.track_number, s.genre, s.year, s.lyrics_url " +
                "FROM songs s " +
                "INNER JOIN playlist_song_cross_ref p ON s.song_id = p.song_id " +
                "WHERE p.playlist_id = :playlistId " +
                "ORDER BY s.track_number ASC"
    )
    suspend fun getSongsInPlaylist(playlistId: Long): List<SongEntity>

    @Query(
        "SELECT song_id, title, album, artist, source, image, duration, " +
                "counter, track_number, genre, year, lyrics_url FROM songs s " +
                "WHERE s.song_id IN (:songIds) " +
                "ORDER BY (" +
                "SELECT updated_at FROM user_song_cross_ref WHERE song_id = s.song_id" +
                ") DESC"
    )
    fun getRecentSongsByIds(songIds: List<String>): Flow<List<SongEntity>>

    @Query(
        "SELECT song_id, title, album, artist, source, image, duration, " +
                "counter, track_number, genre, year, lyrics_url FROM songs s " +
                "WHERE s.song_id IN (:songIds) " +
                "ORDER BY (" +
                "SELECT replay FROM user_song_cross_ref WHERE song_id = s.song_id" +
                ") DESC"
    )
    fun getMostListenedSongsByIds(songIds: List<String>): Flow<List<SongEntity>>

    @Query(
        "SELECT song_id, title, album, artist, source, image, duration, " +
                "counter, track_number, genre, year, lyrics_url FROM songs s " +
                "WHERE s.song_id IN (:songIds) " +
                "ORDER BY (" +
                "SELECT created_at FROM playlist_song_cross_ref WHERE song_id = s.song_id" +
                ") ASC"
    )
    suspend fun getSongsForPlaylistByIds(songIds: List<String>): List<SongEntity>

    @Query(
        "SELECT song_id, title, album, artist, source, image, duration, " +
                "counter, track_number, genre, year, lyrics_url FROM songs " +
                "ORDER BY counter DESC LIMIT :limit"
    )
    fun getMostHeardSongs(limit: Int): Flow<List<SongEntity>>

    @Query(
        "SELECT song_id, title, album, artist, source, image, duration, " +
                "counter, track_number, genre, year, lyrics_url FROM songs WHERE song_id = :id"
    )
    suspend fun getSongById(id: String): SongEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(songModels: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(vararg songModel: SongEntity)

    @Delete
    suspend fun delete(songModel: SongEntity)

    @Query("DELETE FROM songs")
    suspend fun clearAll()

    @Query("UPDATE songs SET counter = counter + 1 WHERE song_id = :songId")
    suspend fun updateSongCounter(songId: String)

    @Query(
        "SELECT s.* FROM songs s " +
                "JOIN artist_song_cross_ref ac ON s.song_id = ac.song_id " +
                "WHERE ac.artist_id = :artistId"
    )
    suspend fun getSongByArtist(artistId: Int): List<SongEntity>
}