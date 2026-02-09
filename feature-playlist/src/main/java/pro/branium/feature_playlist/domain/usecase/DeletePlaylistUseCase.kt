package pro.branium.feature_playlist.domain.usecase

import pro.branium.core_model.PlaylistModel
import pro.branium.core_domain.playlist.repository.PlaylistRepository
import javax.inject.Inject

class DeletePlaylistUseCase @Inject constructor(private val repository: PlaylistRepository) {
    suspend operator fun invoke(playlistModel: PlaylistModel) {
        repository.deletePlaylist(playlistModel)
    }
}