package pro.branium.core_utils

object OptionMenuUtils {
    enum class OptionMenu(val value: String) {
        DOWNLOAD("download"),
        ADD_TO_FAVOURITES("add_favorite"),
        ADD_TO_PLAYLIST("add_playlist"),
        ADD_TO_QUEUE("add_queue"),
        VIEW_ALBUM("view_album"),
        VIEW_ARTIST("view_artist"),
        BLOCK("block"),
        REPORT_ERROR("report_error"),
        VIEW_SONG_INFORMATION("view_song_information"),
        NONE("")
    }
}