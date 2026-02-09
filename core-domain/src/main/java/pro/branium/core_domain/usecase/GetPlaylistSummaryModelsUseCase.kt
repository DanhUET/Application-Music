package pro.branium.core_domain.usecase

import pro.branium.core_domain.playlist.repository.PlaylistRepository
import javax.inject.Inject

class GetPlaylistSummaryModelsUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    operator fun invoke(limit: Int) = repository.getPlaylistsWithCount(limit)
}