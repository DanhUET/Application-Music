package pro.branium.feature.search.domain.usecase

import pro.branium.feature.search.domain.repository.SongSearchingRepository
import javax.inject.Inject

class InsertHistorySearchedSongUseCase @Inject constructor(
    private val repository: SongSearchingRepository
) {
    suspend operator fun invoke(songId: String) {
        repository.insertCrossRef(songId)
    }
}