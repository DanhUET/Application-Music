package pro.branium.core_database.dao.album

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import pro.branium.core_database.entity.album.AlbumEntity
import pro.branium.core_database.entity.album.AlbumListItem
import pro.branium.core_database.entity.album.AlbumWithSongs

@Dao
interface AlbumDao {
    @Query(
        "SELECT album_id, name, artist_name, artwork_url " +
                "FROM albums ORDER BY album_id DESC"
    )
    fun albumPagingSource(): PagingSource<Int, AlbumListItem>

    @Query(
        "SELECT album_id, name, artist_name, artwork_url " +
                "FROM albums ORDER BY album_id DESC LIMIT :limit"
    )
    fun getNAlbumPagingSource(limit: Int): PagingSource<Int, AlbumListItem>

    @Query(
        "SELECT album_id, name, artist_name, artwork_url " +
                "FROM albums ORDER BY album_id DESC LIMIT :limit"
    )
    fun getTopAlbums(limit: Int): List<AlbumListItem>

    @Query(
        "SELECT album_id, name, artist_name, artwork_url, " +
                "release_date, genre, album_type, play_count " +
                "FROM albums WHERE album_id = :albumId"
    )
    suspend fun getAlbumById(albumId: Int): AlbumEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)

    @Query("DELETE FROM albums WHERE album_id = :albumId")
    suspend fun deleteById(albumId: Int)

    @Query("DELETE FROM albums")
    suspend fun clearAll()

    @Transaction
    @Query("SELECT * FROM albums WHERE album_id = :albumId")
    suspend fun getAlbumWithSongs(albumId: Int): AlbumWithSongs?

    @Transaction
    @Query("SELECT * FROM albums")
    suspend fun getAllAlbumWithSongs(): List<AlbumWithSongs>
}