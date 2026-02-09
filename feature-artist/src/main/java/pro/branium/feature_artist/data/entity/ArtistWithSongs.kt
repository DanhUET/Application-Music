package pro.branium.feature_artist.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pro.branium.core_database.entity.song.SongEntity

data class ArtistWithSongs(
    @Embedded
    val artist: ArtistEntity? = null,

    @Relation(
        parentColumn = "artist_id",
        entityColumn = "song_id",
        associateBy = Junction(ArtistSongCrossRefEntity::class)
    )
    val songs: List<SongEntity> = emptyList()
)