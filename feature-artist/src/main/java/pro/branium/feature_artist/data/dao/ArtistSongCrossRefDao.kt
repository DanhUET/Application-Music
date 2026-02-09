package pro.branium.feature_artist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.branium.feature_artist.data.entity.ArtistSongCrossRefEntity

@Dao
interface ArtistSongCrossRefDao {
    @Query("SELECT song_id FROM artist_song_cross_ref WHERE artist_id = :artistId")
    suspend fun getSongIdsForArtist(artistId: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCrossRef(vararg artistSongCrossRefEntity: ArtistSongCrossRefEntity)

    @Delete
    suspend fun deleteCrossRef(vararg artistSongCrossRefEntity: ArtistSongCrossRefEntity)

    @Query("DELETE FROM artist_song_cross_ref")
    suspend fun clearAll()
}