package pro.branium.feature_favorite.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.feature_favorite.data.entity.FavoriteEntity

@Dao
interface FavoriteSongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    @Query(
        "SELECT 1 FROM favorites WHERE user_id =:userId AND song_id = :songId"
    )
    fun isFavorite(userId: Int, songId: String): Flow<Boolean>

    @Query(
        "SELECT songs.song_id, songs.title, songs.album, songs.artist, songs.source, " +
                "songs.image, songs.duration, songs.counter, songs.track_number, " +
                "songs.genre, songs.year, songs.lyrics_url FROM songs " +
                "INNER JOIN favorites ON favorites.song_id = songs.song_id " +
                "WHERE favorites.user_id = :userId "
    )
    fun getFavoriteSongs(userId: Int): Flow<List<SongEntity>>

    @Query(
        "SELECT songs.song_id, songs.title, songs.album, songs.artist, songs.source, " +
                "songs.image, songs.duration, songs.counter, songs.track_number, " +
                "songs.genre, songs.year, songs.lyrics_url FROM songs " +
                "INNER JOIN favorites ON favorites.song_id = songs.song_id " +
                "WHERE favorites.user_id = :userId LIMIT :limit"
    )
    fun getLimitedFavoriteSongs(userId: Int, limit: Int): Flow<List<SongEntity>>
}