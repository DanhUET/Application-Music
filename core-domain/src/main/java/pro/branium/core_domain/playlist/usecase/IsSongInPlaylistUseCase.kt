package pro.branium.core_domain.playlist.usecase

import pro.branium.core_domain.playlist.repository.PlaylistRepository
import javax.inject.Inject

class IsSongInPlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository
){
    suspend operator fun invoke(playlistId: Long, songId: String): Boolean {
        return repository.isSongInPlaylist(playlistId, songId)
    }
}