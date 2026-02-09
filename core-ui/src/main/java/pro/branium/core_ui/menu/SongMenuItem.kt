package pro.branium.core_ui.menu

import pro.branium.core_model.action.SongAction

data class SongMenuItem(
    val id: String,
    val textRes: Int,
    val icon: Int,
    val stateIcon: Int? = null,
    val action: SongAction
)