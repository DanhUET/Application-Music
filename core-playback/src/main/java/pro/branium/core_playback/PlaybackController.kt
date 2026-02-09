package pro.branium.core_playback

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import pro.branium.core_model.PlaylistModel
import pro.branium.core_model.SongModel

interface PlaybackController {
    fun prepareToPlay(
        owner: LifecycleOwner,
        networkAvailable: Boolean,
        onShowNetworkError: () -> Unit,
        songModel: SongModel,
        playlist: PlaylistModel? = null,
        index: Int = -1
    )

    fun showOptionMenu(
        networkAvailable: Boolean,
        onShowNetworkError: () -> Unit,
        fragmentManager: FragmentManager,
        songModel: SongModel
    )

    suspend fun play(song: SongModel? = null)
}