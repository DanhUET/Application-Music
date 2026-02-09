package pro.branium.core_playback.usecase

import pro.branium.core_domain.repository.PlaybackStateRepository
import pro.branium.core_model.PlaylistModel
import pro.branium.core_playback.MediaPlaybackController
import pro.branium.core_playback.mapper.toMediaItems
import javax.inject.Inject

class SetCurrentPlaylistUseCase @Inject constructor(
    private val getCurrentPlaylistUseCase: GetCurrentPlaylistUseCase,
    private val playbackStateRepository: PlaybackStateRepository,
    private val mediaPlaybackController: MediaPlaybackController
) {
    suspend operator fun invoke(newPlaylist: PlaylistModel, startIndex: Int = 0) {
        val current = getCurrentPlaylistUseCase().value
        if (current != newPlaylist || current.songs.size != newPlaylist.songs.size) {
            // update state repository
            playbackStateRepository.updateCurrentPlaylist(newPlaylist)

            // set vào MediaController
            val mediaItems = newPlaylist.songs.toMediaItems()
            mediaPlaybackController.setPlaylist(mediaItems, startIndex)
        }
    }
}
