package pro.branium.feature_song.action.handler

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.branium.core_domain.GetPlaylistByIdUseCase
import pro.branium.core_domain.playlist.result.AddSongResult
import pro.branium.core_domain.playlist.usecase.AddSongToPlaylistUseCase
import pro.branium.core_model.action.SongAction
import pro.branium.core_model.event.UiEvent
import pro.branium.core_navigation.navigator.PlaylistNavigator
import pro.branium.core_resources.R
import pro.branium.core_ui.event.UiEventBus
import pro.branium.feature_song.action.SongActionHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddToPlaylistActionHandler @Inject constructor(
    private val playlistNavigator: PlaylistNavigator,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val getPlaylistUseCase: GetPlaylistByIdUseCase,
    private val uiEventBus: UiEventBus
) : SongActionHandler {

    override suspend fun handle(action: SongAction, anchorView: View?) {
        if (action is SongAction.AddToPlaylist) {
            val fragmentManager = anchorView
                ?.findFragment<Fragment>()
                ?.parentFragmentManager
                ?: return
            val fragment = anchorView.findFragment<Fragment>()
            val parent = fragment.requireActivity().findViewById<View>(android.R.id.content)
            val songId = action.songId

            playlistNavigator.openAddToPlaylistDialog(
                fragmentManager,
                onPlaylistSelected = { playlistId ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = addSongToPlaylistUseCase(playlistId, songId)
                        when (result) {
                            AddSongResult.Added -> {
                                val playlistName = getPlaylistUseCase(playlistId)?.name
                                val message = parent.context.getString(
                                    R.string.add_to_playlist_success,
                                    playlistName
                                )
                                uiEventBus.emit(UiEvent.ShowMessage(message))
                            }

                            AddSongResult.AlreadyExists -> {
                                val message = parent.context
                                    .getString(R.string.add_to_playlist_failed)
                                uiEventBus.emit(UiEvent.ShowMessage(message))
                            }

                            AddSongResult.Error -> {
                                val message = parent.context
                                    .getString(R.string.add_to_playlist_error)
                                uiEventBus.emit(UiEvent.ShowMessage(message))
                            }
                        }
                    }
                }
            )
        }
    }
}
