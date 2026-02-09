package pro.branium.core_domain
import pro.branium.core_domain.playlist.repository.PlaylistRepository
import javax.inject.Inject

class GetPlaylistByIdUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: Long) =
        repository.getPlaylistById(playlistId)
}