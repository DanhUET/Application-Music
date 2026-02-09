package pro.branium.feature.search.domain.usecase

import pro.branium.feature.search.domain.repository.HistorySearchedKeyRepository
import javax.inject.Inject

class InsertHistorySearchedKeyUseCase @Inject constructor(
    private val repository: HistorySearchedKeyRepository
) {
    suspend operator fun invoke(key: String): Long {
        return repository.insertKey(key)
    }
}