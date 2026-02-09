package pro.branium.feature_song.ui.menu

import pro.branium.core_resources.R
import pro.branium.core_utils.OptionMenuUtils


data class SongOptionMenuItem(
    val option: OptionMenuUtils.OptionMenu = OptionMenuUtils.OptionMenu.NONE,
    var iconId: Int = R.drawable.ic_album,
    var menuItemTitle: Int = R.string.app_name
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongOptionMenuItem

        return option == other.option
    }

    override fun hashCode(): Int {
        return option.hashCode()
    }
}
