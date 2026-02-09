package pro.branium.feature_song.action

import android.view.View
import pro.branium.core_model.action.SongAction

interface SongActionHandler {
    suspend fun handle(action: SongAction, anchorView: View? = null)
}