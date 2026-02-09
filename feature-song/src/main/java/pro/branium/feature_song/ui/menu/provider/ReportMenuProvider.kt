package pro.branium.feature_song.ui.menu.provider

import pro.branium.core_resources.R
import pro.branium.core_model.SongModel
import pro.branium.core_model.action.SongAction
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.core_ui.menu.SongMenuProvider
import javax.inject.Inject

class ReportMenuProvider @Inject constructor() : SongMenuProvider {
    override fun provide(song: SongModel): SongMenuItem {
        return SongMenuItem(
            id = "report",
            textRes = R.string.item_report_error,
            icon = R.drawable.ic_report,
            stateIcon = null,
            action = SongAction.ReportError(song.id)
        )
    }
}