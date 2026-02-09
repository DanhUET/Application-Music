package pro.branium.feature_playlist.domain.usecase

import pro.branium.core_domain.playlist.repository.PlaylistRepository
import pro.branium.core_model.PlaylistModel
import pro.branium.core_utils.generator.PlaylistIdGenerator
import javax.inject.Inject

class CreatePlaylistUseCase @Inject constructor(private val repository: PlaylistRepository) {
    suspend operator fun invoke(name: String): PlaylistModel {
        val playlistModel = PlaylistModel(
            playlistId = PlaylistIdGenerator.newUserPlaylistId(),
            name = name
        )
        repository.createPlaylist(playlistModel)
        return playlistModel
    }
}