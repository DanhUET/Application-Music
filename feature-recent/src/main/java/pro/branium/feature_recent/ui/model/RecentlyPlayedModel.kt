package pro.branium.feature_recent.ui.model

import pro.branium.core_model.SongModel

data class RecentlyPlayedModel(
    val song: SongModel,
    val playedAtTimestamp: Long
)