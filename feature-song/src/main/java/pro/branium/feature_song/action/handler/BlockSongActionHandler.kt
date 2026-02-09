package pro.branium.feature_song.action.handler

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import pro.branium.core_model.action.SongAction
import pro.branium.core_resources.R
import pro.branium.feature_song.action.SongActionHandler
import pro.branium.feature_song.domain.repository.BlockRepository
import javax.inject.Inject

class BlockSongActionHandler @Inject constructor(
    private val blockRepository: BlockRepository
) : SongActionHandler {
    override suspend fun handle(action: SongAction, anchorView: View?) {
        if (action is SongAction.Block) {
            blockRepository.block(action.songId)
            anchorView?.let {
                Snackbar.make(
                    it,
                    R.string.block_song_success,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}