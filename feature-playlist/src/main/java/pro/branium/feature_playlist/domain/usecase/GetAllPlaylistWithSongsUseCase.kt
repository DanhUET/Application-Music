package pro.branium.feature_playlist.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.PlaylistModel
import pro.branium.core_domain.playlist.repository.PlaylistRepository
import javax.inject.Inject

class GetAllPlaylistWithSongsUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    operator fun invoke(): Flow<List<PlaylistModel>> = repository.getAllPlaylistsWithSongs()
}