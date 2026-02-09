package pro.branium.feature_artist.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pro.branium.feature_artist.data.entity.ArtistEntity
import pro.branium.feature_artist.data.entity.ArtistWithSongs

@Dao
interface ArtistDao {
    @get:Query("SELECT artist_id, name, avatar, interested FROM artists")
    val artists: Flow<List<ArtistEntity>>

    @Query("SELECT  artist_id, name, avatar, interested  FROM artists")
    fun artistPagingSource(): PagingSource<Int, ArtistEntity>

    @Query("SELECT artist_id, name, avatar, interested FROM artists LIMIT :limit")
    fun getNArtistPagingSource(limit: Int): PagingSource<Int, ArtistEntity>

    @get:Query("SELECT * FROM artists LIMIT 15")
    val top15Artists: Flow<List<ArtistEntity>>

    @Transaction
    @Query(
        "SELECT artist_id, name, avatar, interested " +
                "FROM artists WHERE artist_id = :artistId"
    )
    suspend fun getArtistWithSongs(artistId: Int): ArtistWithSongs?

//    @Transaction
//    @Query("SELECT * FROM artists WHERE artist_id = :artistId")
//    suspend fun getSongsForArtist(artistId: Int): List<Song>

    @Query(
        "SELECT artist_id, name, avatar, interested " +
                "FROM artists WHERE artist_id = :id"
    )
    fun getArtistById(id: Int): Flow<ArtistEntity?>

    @Query(
        "SELECT artist_id, name, avatar, interested " +
                "FROM artists WHERE name = :name"
    )
    suspend fun getArtistByName(name: String): ArtistEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg artistModels: ArtistEntity)

    @Delete
    suspend fun delete(artistModel: ArtistEntity)

    @Query("DELETE FROM artists")
    suspend fun clearAll()

    @Update
    suspend fun update(artistModel: ArtistEntity)
}