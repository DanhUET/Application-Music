package pro.branium.feature.search.domain.usecase

import pro.branium.feature.search.domain.repository.HistorySearchedKeyRepository
import javax.inject.Inject

class InsertUserHistorySearchedKeyUseCase @Inject constructor(
    private val repository: HistorySearchedKeyRepository
) {
    suspend operator fun invoke(keyId: Long) {
        repository.insertKeyCrossRef(keyId)
    }
}