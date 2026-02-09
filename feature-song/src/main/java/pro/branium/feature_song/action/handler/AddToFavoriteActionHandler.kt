package pro.branium.feature_song.action.handler

import android.view.View
import com.google.android.material.snackbar.Snackbar
import pro.branium.core_domain.repository.FavoriteRepository
import pro.branium.core_model.action.SongAction
import pro.branium.core_resources.R
import pro.branium.feature_song.action.SongActionHandler
import javax.inject.Inject

class AddToFavoriteActionHandler @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : SongActionHandler {
    override suspend fun handle(action: SongAction, anchorView: View?) {
        if (action is SongAction.AddToFavorite) {
            val isFavorite = favoriteRepository.toggleFavorite(action.songId)
            val messageId = if(isFavorite) {
                R.string.add_to_favorite_success
            } else {
                R.string.remove_from_favorite_success
            }
            anchorView?.let {
                Snackbar.make(it, messageId, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}