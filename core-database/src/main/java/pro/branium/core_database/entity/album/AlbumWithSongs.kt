package pro.branium.core_database.entity.album

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pro.branium.core_database.entity.song.SongEntity

data class AlbumWithSongs(
    @Embedded
    val album: AlbumEntity? = null,

    @Relation(
        parentColumn = "album_id",
        entityColumn = "song_id",
        associateBy = Junction(AlbumSongCrossRefEntity::class)
    )
    val songs: List<SongEntity> = emptyList()
)