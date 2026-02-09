package pro.branium.feature_playlist.ui.menu

import pro.branium.feature_playlist.R

enum class PlaylistOptionMenu(
    val iconRes: Int,
    val titleRes: Int
) {
    VIEW_DETAIL(R.drawable.ic_information,
        pro.branium.core_resources.R.string.item_view_detail),
    UPDATE(R.drawable.ic_edit,
        pro.branium.core_resources.R.string.item_rename_playlist),
    DELETE(R.drawable.ic_delete,
        pro.branium.core_resources.R.string.item_delete),
    SYNCHRONIZE(R.drawable.ic_sync,
        pro.branium.core_resources.R.string.item_synchronize);

    fun toMenuItem(): PlaylistOptionMenuItem =
        PlaylistOptionMenuItem(option = this, iconId = iconRes, menuItemTitle = titleRes)
}

data class PlaylistOptionMenuItem(
    val option: PlaylistOptionMenu,
    val iconId: Int,
    val menuItemTitle: Int
)