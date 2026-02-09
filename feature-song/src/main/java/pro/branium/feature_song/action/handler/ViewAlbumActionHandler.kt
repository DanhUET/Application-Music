package pro.branium.feature_song.action.handler

import android.view.View
import pro.branium.core_model.action.SongAction
import pro.branium.core_navigation.navigator.AlbumNavigator
import pro.branium.feature_song.action.SongActionHandler
import javax.inject.Inject

class ViewAlbumActionHandler @Inject constructor(
    private val navigator: AlbumNavigator
) : SongActionHandler {
    override suspend fun handle(action: SongAction, anchorView: View?) {
        if (action is SongAction.ViewAlbum) {
            navigator.openAlbumDetail(action.albumId)
        }
    }
}