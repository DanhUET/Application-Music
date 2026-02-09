package pro.branium.feature_recent.data.datasource.entity

import pro.branium.core_database.entity.song.SongEntity

data class RecentlyPlayedEntity(
    val song: SongEntity,
    val playedAtTimestamp: Long
)