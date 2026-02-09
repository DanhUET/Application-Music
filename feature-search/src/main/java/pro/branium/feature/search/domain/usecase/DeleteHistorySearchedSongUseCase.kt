package pro.branium.feature.search.domain.usecase

import pro.branium.feature.search.domain.repository.SongSearchingRepository
import javax.inject.Inject

class DeleteHistorySearchedSongUseCase @Inject constructor(
    private val repository: SongSearchingRepository
) {
    suspend operator fun invoke(songId: String) {
        repository.deleteCrossRef(songId)
    }
}