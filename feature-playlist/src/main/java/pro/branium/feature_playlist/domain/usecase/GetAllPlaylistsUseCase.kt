package pro.branium.feature_playlist.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.PlaylistModel
import pro.branium.core_domain.playlist.repository.PlaylistRepository
import pro.branium.core_model.PlaylistSummary
import pro.branium.feature_playlist.ui.model.PlaylistItemModel
import javax.inject.Inject

class GetAllPlaylistsUseCase @Inject constructor(private val repository: PlaylistRepository) {
    operator fun invoke() : Flow<List<PlaylistSummary>> = repository.getAllPlaylists()
}