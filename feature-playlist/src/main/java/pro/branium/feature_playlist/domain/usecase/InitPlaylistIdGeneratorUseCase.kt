package pro.branium.feature_playlist.domain.usecase

import pro.branium.core_domain.playlist.repository.PlaylistRepository
import pro.branium.core_utils.generator.PlaylistIdGenerator
import javax.inject.Inject

class InitPlaylistIdGeneratorUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke() {
        val maxId = playlistRepository.getMaxPlaylistId() ?: 10000L
        PlaylistIdGenerator.init(maxId)
    }
}
