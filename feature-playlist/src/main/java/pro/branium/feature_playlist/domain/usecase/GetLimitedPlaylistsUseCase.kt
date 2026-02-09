package pro.branium.feature_playlist.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.PlaylistModel
import pro.branium.core_domain.playlist.repository.PlaylistRepository
import javax.inject.Inject

class GetLimitedPlaylistsUseCase @Inject constructor(private val repository: PlaylistRepository) {
    operator fun invoke(limit: Int): Flow<List<PlaylistModel>> =
        repository.getLimitedPlaylists(limit)
}