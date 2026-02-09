package pro.branium.feature_song.action.handler

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import pro.branium.core_model.action.SongAction
import pro.branium.core_resources.R
import pro.branium.feature_song.action.SongActionHandler
import pro.branium.feature_song.domain.repository.ReportRepository
import javax.inject.Inject

class ReportErrorActionHandler @Inject constructor(
    private val reportRepository: ReportRepository
) : SongActionHandler {
    override suspend fun handle(action: SongAction, anchorView: View?) {
        if (action is SongAction.ReportError) {
            reportRepository.report(action.songId)
            anchorView?.let {
                Snackbar.make(
                    it,
                    R.string.report_song_success,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}