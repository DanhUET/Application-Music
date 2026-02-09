package pro.branium.feature.search.data.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.branium.core_database.entity.song.SongEntity

@Dao
interface SearchingDao {
    @Query("SELECT * FROM songs WHERE title LIKE :query OR artist LIKE :query")
    suspend fun search(query: String): List<SongEntity>

    @Query(
        "SELECT * FROM songs s WHERE s.song_id IN (:songIds) " +
                "ORDER BY (" +
                "SELECT created_at FROM user_searched_song_cross_ref WHERE song_id = s.song_id" +
                ") DESC"
    )
    fun getHistorySearchedSongsForUserByIds(songIds: List<String>): Flow<List<SongEntity>>
}