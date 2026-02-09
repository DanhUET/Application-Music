package pro.branium.core_ui.mapper

import pro.branium.core_model.PlaylistModel
import pro.branium.core_ui.model.DisplaySongModel

fun List<DisplaySongModel>.markNowPlaying(
    currentPlaylist: PlaylistModel?,
    currentId: String?,
    isPlaying: Boolean,
    vararg validPlaylistIds: Long
): List<DisplaySongModel> {
    return map { m ->
        if (currentPlaylist != null && validPlaylistIds.contains(currentPlaylist.playlistId)) {
            m.copy(
                isNowPlaying = (m.song.id == currentId),
                isPlaying = isPlaying && (m.song.id == currentId)
            )
        } else {
            m
        }
    }
}
