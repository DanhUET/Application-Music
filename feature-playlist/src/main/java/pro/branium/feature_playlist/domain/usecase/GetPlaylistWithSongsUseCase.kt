package pro.branium.feature_playlist.domain.usecase

import pro.branium.core_domain.playlist.repository.PlaylistRepository
import javax.inject.Inject

class GetPlaylistWithSongsUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    operator fun invoke(playlistId: Long) =
        repository.getPlaylistsWithSongsByPlaylistId(playlistId)
}