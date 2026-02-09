package pro.branium.core_database.dao.album

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.branium.core_database.entity.album.AlbumSongCrossRefEntity

@Dao
interface AlbumSongCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCrossRef(vararg albumSongCrossRefEntity: AlbumSongCrossRefEntity)

    @Delete
    suspend fun deleteCrossRef(vararg albumSongCrossRefEntity: AlbumSongCrossRefEntity)

    @Query("DELETE FROM album_song_cross_ref")
    suspend fun clearAll()
}