package pro.branium.feature.search.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pro.branium.core_database.entity.song.SongEntity
import pro.branium.core_database.entity.user.UserEntity
import pro.branium.core_database.entity.user.UserSearchedSongCrossRefEntity

data class HistorySearchedSongsEntity(
    @Embedded
    val userModel: UserEntity,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "song_id",
        associateBy = Junction(UserSearchedSongCrossRefEntity::class)
    )
    val songs: List<SongEntity> = emptyList()
)