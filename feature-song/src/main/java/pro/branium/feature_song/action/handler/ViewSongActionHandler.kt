package pro.branium.feature_song.action.handler

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import pro.branium.core_model.action.SongAction
import pro.branium.core_navigation.navigator.SongInfoNavigator
import pro.branium.feature_song.action.SongActionHandler
import javax.inject.Inject

class ViewSongActionHandler @Inject constructor(
    private val songInfoNavigator: SongInfoNavigator
) : SongActionHandler {
    override suspend fun handle(action: SongAction, anchorView: View?) {
        if (action is SongAction.ViewSong) {
            val fragmentManager = anchorView
                ?.findFragment<Fragment>()
                ?.parentFragmentManager
                ?: return
            songInfoNavigator.openSongInfo(fragmentManager, action.songId)
        }
    }
}