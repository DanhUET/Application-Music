package pro.branium.feature_playlist.mapper

import pro.branium.core_model.PlaylistSummary
import pro.branium.feature_playlist.ui.model.PlaylistItemModel

fun PlaylistSummary.toUiModel(): PlaylistItemModel {
    return PlaylistItemModel(
        playlistId = playlistId,
        name = name,
        artwork = artwork,
        songCount = songCount
    )
}

fun List<PlaylistSummary>.toUiModels(): List<PlaylistItemModel> {
    return map { it.toUiModel() }
}