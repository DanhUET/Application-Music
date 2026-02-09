package pro.branium.feature_mostheard.domain.usecase

import pro.branium.core_domain.repository.SongRepository
import javax.inject.Inject

class GetMostHeardSongsUseCase @Inject constructor(private val repository: SongRepository) {
    operator fun invoke(limit: Int) = repository.getMostHeardSongs(limit)
}