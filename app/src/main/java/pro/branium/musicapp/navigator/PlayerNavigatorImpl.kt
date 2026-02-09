package pro.branium.musicapp.navigator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import pro.branium.core_navigation.navigator.PlayerNavigator
import pro.branium.feature.player.ui.fragment.NowPlayingFragment
import javax.inject.Inject

class PlayerNavigatorImpl @Inject constructor(
    private val context: Context
) : PlayerNavigator {
    override fun navigateToNowPlaying() {
        val activity = context as? AppCompatActivity ?: return
        NowPlayingFragment().show(activity.supportFragmentManager, "PlayerFragment")
    }
}