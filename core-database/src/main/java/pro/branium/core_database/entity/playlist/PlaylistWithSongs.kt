package pro.branium.core_database.entity.playlist

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pro.branium.core_database.entity.song.SongEntity

data class PlaylistWithSongs(
    @Embedded
    val playlist: PlaylistEntity,

    @Relation(
        parentColumn = "playlist_id",
        entityColumn = "song_id",
        associateBy = Junction(PlaylistSongCrossRefEntity::class)
    )
    var songs: List<SongEntity> = emptyList()
)