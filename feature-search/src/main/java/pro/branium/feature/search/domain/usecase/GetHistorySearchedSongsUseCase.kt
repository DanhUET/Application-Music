package pro.branium.feature.search.domain.usecase

import pro.branium.feature.search.domain.repository.SongSearchingRepository
import javax.inject.Inject

class GetHistorySearchedSongsUseCase @Inject constructor(
    private val repository: SongSearchingRepository
) {
    operator fun invoke() = repository.getHistorySearchedSongsForUser()
}