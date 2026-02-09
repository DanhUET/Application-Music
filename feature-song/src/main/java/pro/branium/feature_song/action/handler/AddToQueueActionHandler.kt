package pro.branium.feature_song.action.handler

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import pro.branium.core_domain.manager.QueueManager
import pro.branium.core_model.action.SongAction
import pro.branium.core_resources.R
import pro.branium.feature_song.action.SongActionHandler
import javax.inject.Inject

class AddToQueueActionHandler @Inject constructor(
    private val queueManager: QueueManager
) : SongActionHandler {
    override suspend fun handle(action: SongAction, anchorView: View?) {
        if (action is SongAction.AddToQueue) {
            queueManager.addToQueue(action.songId)
            anchorView?.let {
                Snackbar.make(
                    it,
                    R.string.add_to_queue_success,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}