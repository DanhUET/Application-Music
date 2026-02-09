package pro.branium.feature_song.action.handler

import android.view.View
import pro.branium.core_model.action.SongAction
import pro.branium.core_navigation.navigator.ArtistNavigator
import pro.branium.feature_song.action.SongActionHandler
import javax.inject.Inject

class ViewArtistActionHandler @Inject constructor(
    private val artistNavigator: ArtistNavigator
) : SongActionHandler {
    override suspend fun handle(action: SongAction, anchorView: View?) {
        if (action is SongAction.ViewArtist) {
            artistNavigator.openArtistDetail(action.artistId)
        }
    }
}