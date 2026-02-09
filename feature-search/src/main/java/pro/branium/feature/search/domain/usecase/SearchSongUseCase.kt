package pro.branium.feature.search.domain.usecase

import pro.branium.core_model.SongModel
import pro.branium.feature.search.domain.repository.SongSearchingRepository
import javax.inject.Inject

class SearchSongUseCase @Inject constructor(
    private val repository: SongSearchingRepository
) {
    suspend operator fun invoke(query: String): List<SongModel> = repository.search(query)
}