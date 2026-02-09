package pro.branium.feature_playlist.ui.menu

object OptionMenuUtils {
    @JvmStatic
    val playlistOptionMenuItems: List<PlaylistOptionMenuItem> =
        PlaylistOptionMenu.entries.map { it.toMenuItem() }
}