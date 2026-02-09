package pro.branium.core_utils

import pro.branium.core_model.PlaylistModel


object MusicAppUtils {
    const val EXTRA_CURRENT_FRACTION = "EXTRA_CURRENT_FRACTION"
    const val EXTRA_NETWORK_STATE = "EXTRA_NETWORK_STATE"
    const val PREF_SONG_ID = "PREF_SONG_ID"
    const val PREF_CURRENT_POSITION = "PREF_CURRENT_POSITION"
    const val PREF_PLAYLIST_ID = "PREF_PLAYLIST_ID"
    const val PREF_FILE_NAME = "pro.branium.musicapp.preff_file"
    const val PREF_CURRENT_USER_ID = "PREF_CURRENT_USER_ID"
    const val DEFAULT_MARGIN_END = 48
    const val ALBUM_PAGE_SIZE = 10
    const val ARTIST_PAGE_SIZE = 20
    const val RECOMMENDED_PAGE_SIZE = 20
    const val CACHE_TIMEOUT = 168L
    const val EXTRA_PLAYLIST_ID = "EXTRA_PLAYLIST_ID"
    const val ACTION_PLAY_PLAYLIST = "ACTION_PLAY_PLAYLIST"

    @JvmField
    var DENSITY: Float = 0f

    val defaultPlaylists = mapOf(
        0L to PlaylistModel(playlistId = 0, name = DefaultPlaylistName.DEFAULT.value),
        1L to PlaylistModel(playlistId = 1, name = DefaultPlaylistName.RECOMMENDED.value),
        2L to PlaylistModel(playlistId = 2, name = DefaultPlaylistName.FAVORITES.value),
        3L to PlaylistModel(playlistId = 3, name = DefaultPlaylistName.RECENT.value),
        4L to PlaylistModel(playlistId = 4, name = DefaultPlaylistName.MOST_HEARD.value),
        5L to PlaylistModel(playlistId = 5, name = DefaultPlaylistName.FOR_YOU.value),
        6L to PlaylistModel(playlistId = 6, name = DefaultPlaylistName.SEARCH.value),
        7L to PlaylistModel(playlistId = 7, name = DefaultPlaylistName.HISTORY_SEARCH.value),
        8L to PlaylistModel(playlistId = 8, name = DefaultPlaylistName.CUSTOM.value),
        9L to PlaylistModel(playlistId = 9, name = DefaultPlaylistName.MORE_RECOMMENDED.value),
        10L to PlaylistModel(playlistId = 10, name = DefaultPlaylistName.MORE_FAVORITE.value),
    )

    enum class DefaultPlaylistName(val value: String) {
        DEFAULT("Default"),
        FAVORITES("Favorites"),
        MORE_FAVORITE("More Favorites"),
        RECOMMENDED("Recommended"),
        MORE_RECOMMENDED("More Recommended"),
        RECENT("Recent"),
        SEARCH("Search"),
        MOST_HEARD("Most_Heard"),
        FOR_YOU("For_You"),
        HISTORY_SEARCH("history_searched"),
        CUSTOM("Custom")
    }
}